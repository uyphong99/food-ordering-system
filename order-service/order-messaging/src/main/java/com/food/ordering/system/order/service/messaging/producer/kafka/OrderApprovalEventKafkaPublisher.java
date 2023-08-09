package com.food.ordering.system.order.service.messaging.producer.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.food.ordering.system.kafka.producer.service.KafkaProducer;
import com.food.ordering.system.order.service.domain.config.OrderServiceConfigData;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalEventPayload;
import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurantapproval.RestaurantApprovalRequestMessagePublisher;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import com.food.ordering.system.outbox.OutboxStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;
@Component
@Slf4j
@AllArgsConstructor
public class OrderApprovalEventKafkaPublisher implements RestaurantApprovalRequestMessagePublisher {
    private final ObjectMapper objectMapper;
    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer;
    private final OrderServiceConfigData orderServiceConfigData;
    
    @Override
    public void publish(OrderApprovalOutboxMessage orderApprovalOutboxMessage, 
                        BiConsumer<OrderApprovalOutboxMessage, OutboxStatus> outboxCallback) {
        OrderApprovalEventPayload orderApprovalEventPayload = getOrderApprovalEventPayload(orderApprovalOutboxMessage.getPayload());
        String sagaId = orderApprovalOutboxMessage.getSagaId().toString();

        log.info("Received OrderApprovalOutboxMessage for order id: {} and saga id: {}",
                orderApprovalEventPayload.getOrderId(), sagaId);

        try {
            RestaurantApprovalRequestAvroModel restaurantApprovalRequestAvroModel = orderMessagingDataMapper
                    .approvalEventToApprovalRequestAvroModel(sagaId, orderApprovalEventPayload);
            kafkaProducer.send(orderServiceConfigData.getRestaurantApprovalRequestTopicName(), sagaId,
                    restaurantApprovalRequestAvroModel, outboxCallback, orderApprovalOutboxMessage);

            log.info("OrderApprovalEventPayload is sent to the kafka for order id: {} and saga id: {}",
                    orderApprovalEventPayload.getOrderId(), sagaId);
        } catch (Exception e) {
            log.info("Error while sending OrderApprovalEventPayload " +
                    "to kafka with order id: {} and saga id: {}, error: {}", orderApprovalEventPayload.getOrderId(), sagaId, e.getMessage());
        }
    }

    private OrderApprovalEventPayload getOrderApprovalEventPayload(String payload) {
        try {
            return objectMapper.readValue(payload, OrderApprovalEventPayload.class);
        } catch (JsonProcessingException e) {
            log.error("Could not read OrderApprovalEventPayload object!", e);
            throw new OrderDomainException("Could not read OrderApprovalPayload object", e);
        }
    }
}
