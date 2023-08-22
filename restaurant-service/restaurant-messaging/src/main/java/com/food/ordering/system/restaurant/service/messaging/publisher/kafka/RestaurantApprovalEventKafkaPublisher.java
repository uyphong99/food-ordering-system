package com.food.ordering.system.restaurant.service.messaging.publisher.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.ordering.system.kafka.producer.service.KafkaProducer;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.restaurant.service.domain.config.RestaurantServiceConfigData;
import com.food.ordering.system.restaurant.service.domain.exception.RestaurantApplicationServiceException;
import com.food.ordering.system.restaurant.service.domain.outbox.model.OrderEventPayload;
import com.food.ordering.system.restaurant.service.domain.outbox.model.OrderOutboxMessage;
import com.food.ordering.system.restaurant.service.domain.ports.output.message.publisher.RestaurantApprovalResponseMessagePublisher;
import com.food.ordering.system.restaurant.service.messaging.mapper.RestaurantMessagingDataMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;
@Slf4j
@Component
@AllArgsConstructor
public class RestaurantApprovalEventKafkaPublisher implements RestaurantApprovalResponseMessagePublisher {
    private final KafkaProducer<String, RestaurantApprovalResponseAvroModel> kafkaProducer;
    private final RestaurantServiceConfigData restaurantServiceConfigData;
    private final RestaurantMessagingDataMapper dataMapper;
    private final ObjectMapper objectMapper;
    @Override
    public void publish(OrderOutboxMessage orderOutboxMessage,
                        BiConsumer<OrderOutboxMessage, OutboxStatus> outboxCallback) {
        String sagaId = orderOutboxMessage.getSagaId().toString();
        OrderEventPayload orderEventPayload = getOrderEventPayload(orderOutboxMessage.getPayload());

        log.info("Received OrderOutboxMessage for order id: {} and saga id: {}", orderEventPayload.getOrderId(), sagaId);

        try {
            kafkaProducer.send(restaurantServiceConfigData.getRestaurantApprovalResponseTopicName(),
                    sagaId,
                    dataMapper.getResponseAvroModel(sagaId, orderEventPayload),
                    outboxCallback,
                    orderOutboxMessage);

            log.info("RestaurantApprovalResponseAvroModel sent to kafka for order id: {} and saga id: {}",
                    orderEventPayload.getOrderId(), sagaId);
        } catch (Exception e) {
            log.error("Error while sending RestaurantApprovalResponseAvroModel message to kafka with order id: {} and saga id: {}, error: {}",
                    orderEventPayload.getOrderId(), sagaId, e.getMessage());
        }
    }

    private OrderEventPayload getOrderEventPayload(String orderEventPayloadString) {
        try {
            OrderEventPayload orderEventPayload = objectMapper.readValue(orderEventPayloadString, OrderEventPayload.class);
        } catch (JsonProcessingException e) {
            log.error("Fail to parse the payload string to OrderEventPayload!");
            throw new RestaurantApplicationServiceException("Fail to parse the payload string to OrderEventPayload!",e);
        }
    }
}
