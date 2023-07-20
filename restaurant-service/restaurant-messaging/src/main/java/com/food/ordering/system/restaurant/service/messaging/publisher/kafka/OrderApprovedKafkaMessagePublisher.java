package com.food.ordering.system.restaurant.service.messaging.publisher.kafka;

import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.ordering.system.kafka.producer.KafkaMessageHelper;
import com.food.ordering.system.kafka.producer.service.KafkaProducer;
import com.food.ordering.system.restaurant.service.domain.config.RestaurantServiceConfigData;
import com.food.ordering.system.restaurant.service.domain.event.OrderApprovedEvent;
import com.food.ordering.system.restaurant.service.domain.ports.output.message.publisher.OrderApprovedMessagePublisher;
import com.food.ordering.system.restaurant.service.messaging.mapper.RestaurantMessagingDataMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Component
@AllArgsConstructor
public class OrderApprovedKafkaMessagePublisher implements OrderApprovedMessagePublisher {
    private RestaurantMessagingDataMapper restaurantMessagingDataMapper;
    private KafkaProducer<String, RestaurantApprovalResponseAvroModel> kafkaProducer;
    private RestaurantServiceConfigData restaurantServiceConfigData;

    @Override
    public void publish(OrderApprovedEvent domainEvent) {
        String orderId = domainEvent.getOrderApproval().getOrderId().getValue().toString();
        String topicName = restaurantServiceConfigData.getRestaurantApprovalResponseTopicName();

        log.info("Receive OrderApprovedEvent for order id: {}", orderId);

        try {
            RestaurantApprovalResponseAvroModel message = restaurantMessagingDataMapper
                    .orderApprovalEventToRestaurantApprovalResponseAvroModel(domainEvent);


            kafkaProducer.send(topicName, orderId, message);

            log.info("RestaurantApprovalResponseAvroModel sent to kafka at {}", System.nanoTime());
        } catch (Exception e) {
            log.error("Error while sending RestaurantApprovalResponseAvroModel message" +
                    " to kafka with order id: {}, error: {}", orderId, e.getMessage());
        }
    }
}
