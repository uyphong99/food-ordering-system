package com.food.ordering.system.payment.service.messaging.publisher.kafka;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.ordering.system.kafka.producer.service.KafkaProducer;
import com.food.ordering.system.payment.service.domain.config.PaymentServiceConfigData;
import com.food.ordering.system.payment.service.domain.event.PaymentFailedEvent;
import com.food.ordering.system.payment.service.messaging.mapper.PaymentMessagingDataMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class PaymentFailedKafkaMessagePublisher implements DomainEventPublisher<PaymentFailedEvent> {
    private final PaymentMessagingDataMapper paymentMessagingDataMapper;
    private final KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer;
    private final PaymentServiceConfigData paymentServiceConfigData;
    //private final KafkaMessageHelper kafkaMessageHelper;
    @Override
    public void publish(PaymentFailedEvent domainEvent) {
        String orderId = domainEvent.getPayment().getOrderId().getValue().toString();
        String paymentResponseTopicName = paymentServiceConfigData.getPaymentResponseTopicName();

        log.info("Received PaymentFailedEvent for orderId: {}", orderId);

        try {
            PaymentResponseAvroModel paymentResponseAvroModel =
                    paymentMessagingDataMapper.paymentEventToPaymentResponseAvroModel(domainEvent);

            kafkaProducer.send(paymentResponseTopicName, orderId, paymentResponseAvroModel);
            log.info("PaymentResponseAvroModel sent to kafka for order id: {}", orderId);
        } catch (Exception e) {
            log.error("Error while sending PaymentResponseAvroModel message" + " to kafka with order id: {}, error: {}", orderId, e.getMessage());
        }
    }
}
