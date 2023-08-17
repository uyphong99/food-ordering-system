package com.food.ordering.system.payment.service.messaging.publisher.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.ordering.system.kafka.producer.service.KafkaProducer;
import com.food.ordering.system.order.service.domain.event.OrderEvent;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.payment.service.domain.config.PaymentServiceConfigData;
import com.food.ordering.system.payment.service.domain.exception.PaymentDomainException;
import com.food.ordering.system.payment.service.domain.outbox.model.OrderEventPayload;
import com.food.ordering.system.payment.service.domain.outbox.model.OrderOutboxMessage;
import com.food.ordering.system.payment.service.domain.ports.output.message.publisher.PaymentResponseMessagePublisher;
import com.food.ordering.system.payment.service.messaging.mapper.PaymentMessagingDataMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;
@Slf4j
@Component
@AllArgsConstructor
public class PaymentEventKafkaPublisher implements PaymentResponseMessagePublisher {
    private final PaymentMessagingDataMapper dataMapper;
    private final KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer;
    private final PaymentServiceConfigData configData;
    private final ObjectMapper objectMapper;
    @Override
    public void publish(OrderOutboxMessage outboxMessage, BiConsumer<OrderOutboxMessage, OutboxStatus> outboxCallback) {
        OrderEventPayload orderEventPayload = getOrderEventPayload(outboxMessage.getPayload());

        String sagaId = outboxMessage.getSagaId().toString();

        log.info("Received OrderOutboxMessage for order id: {} and saga id: {}", orderEventPayload.getOrderId(), sagaId);

        try {
            PaymentResponseAvroModel paymentResponseAvroModel = dataMapper.payloadToAvroModel(sagaId, orderEventPayload);

            kafkaProducer.send(configData.getPaymentResponseTopicName(), sagaId, paymentResponseAvroModel,
                    outboxCallback, outboxMessage);

            log.info("PaymentResponseAvroModel sent to kafka for order id: {} and saga id: {}",
                    paymentResponseAvroModel.getOrderId(), sagaId);
        } catch (Exception e) {
            log.error("Error while sending PaymentRequestAvroModel message to kafka with order id: {} and saga id: {}, error: {}",
                    orderEventPayload.getOrderId(), sagaId, e.getMessage());
        }
    }

    private OrderEventPayload getOrderEventPayload(String payload) {
        try {
            return objectMapper.readValue(payload, OrderEventPayload.class);
        } catch (JsonProcessingException e) {
            log.error("Fail to parse the payload string to OrderEventPayload!");
            throw new PaymentDomainException("Fail to parse the payload string to OrderEventPayload!",e);
        }
    }
}
