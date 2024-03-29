package com.food.ordering.system.order.service.messaging.listener.kafka;

import com.food.ordering.system.domain.valueobject.OrderApprovalStatus;
import com.food.ordering.system.domain.valueobject.PaymentStatus;
import com.food.ordering.system.kafka.consumer.KafkaConsumer;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.ordering.system.kafka.order.avro.model.RestaurantOrderStatus;
import com.food.ordering.system.order.service.domain.dto.message.PaymentResponse;
import com.food.ordering.system.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.exception.OrderNotFoundException;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.restaurantapproval.RestaurantApprovalResponseMessageListener;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;
/**
 * Kafka consumer in order service, this consumer subscribes to Restaurant response topic
 * */
@AllArgsConstructor
@Slf4j
@Component
public class RestaurantApprovalResponseKafkaListener implements KafkaConsumer<RestaurantApprovalResponseAvroModel> {
    private final RestaurantApprovalResponseMessageListener restaurantApprovalResponseMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;

    @Override
    @KafkaListener(id = "${kafka-consumer-config.restaurant-approval-consumer-group-id}",
            topics = "${order-service.restaurant-approval-response-topic-name}")
    public void receive(@Payload List<RestaurantApprovalResponseAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info("{} number of restaurant responses received with keys: {}, partitions: {} and offsets: {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());

        messages.forEach(restaurantResponseAvroModel -> {
            try {
                if (OrderApprovalStatus.APPROVED.toString().equals(restaurantResponseAvroModel.getOrderApprovalStatus().toString())) {
                    log.info("Processing successful restaurant order for saga id: {}", restaurantResponseAvroModel.getSagaId());
                    RestaurantApprovalResponse restaurantResponse =
                            orderMessagingDataMapper.restaurantResponseAvroModelToRestaurantResponse(restaurantResponseAvroModel);
                    restaurantApprovalResponseMessageListener.orderApproved(restaurantResponse);
                } else if (restaurantResponseAvroModel.getOrderApprovalStatus().toString().equals("REJECTED")) {
                    log.info("Fail to process restaurant order for saga id: {} with failure message: {}", restaurantResponseAvroModel.getSagaId(),
                            String.join(Order.FAILURE_MESSAGE_DELIMITER, restaurantResponseAvroModel.getFailureMessages()));
                    RestaurantApprovalResponse restaurantResponse =
                            orderMessagingDataMapper.restaurantResponseAvroModelToRestaurantResponse(restaurantResponseAvroModel);
                    restaurantApprovalResponseMessageListener.orderRejected(restaurantResponse);
                }
            } catch (OptimisticLockingFailureException e) {
                log.error("Caught optimistic locking exception in PaymentResponseKafkaListener for order id: {}",
                        restaurantResponseAvroModel.getOrderId());
            } catch (OrderNotFoundException e) {
                log.error("No order found for order id: {}", restaurantResponseAvroModel.getOrderId());
            }
        });
    }
}
