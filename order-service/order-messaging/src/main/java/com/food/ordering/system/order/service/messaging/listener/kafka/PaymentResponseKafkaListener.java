package com.food.ordering.system.order.service.messaging.listener.kafka;

import com.food.ordering.system.domain.valueobject.PaymentStatus;
import com.food.ordering.system.kafka.consumer.KafkaConsumer;
import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.ordering.system.order.service.domain.dto.message.PaymentResponse;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.payment.PaymentResponseMessageListener;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
@Slf4j
public class PaymentResponseKafkaListener implements KafkaConsumer<PaymentResponseAvroModel> {
    private final PaymentResponseMessageListener paymentResponseMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;

    @Override
    @KafkaListener(id = "${kafka-consumer-config.payment-consumer-group-id}", topics = "${order-service.payment-response-topic-name}")
    public void receive(@Payload List<PaymentResponseAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("{} number of payment responses received with keys: {}, partitions: {} and offsets: {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());

        messages.forEach(paymentResponseAvroModel -> {
            if (PaymentStatus.COMPLETED.toString().equals(paymentResponseAvroModel.getPaymentStatus().toString())) {
                log.info("Processing successful payment for order id: {}", paymentResponseAvroModel.getOrderId());
                PaymentResponse paymentResponse = orderMessagingDataMapper.paymentResponseAvroModelToPaymentResponse(paymentResponseAvroModel);
                paymentResponseMessageListener.paymentCompleted(paymentResponse);
            } else if (paymentResponseAvroModel.getPaymentStatus().toString().equals("CANCELLED") ||
                    paymentResponseAvroModel.getPaymentStatus().toString().equals("FAILED")) {
                log.info("Fail to process payment for order id: {}", paymentResponseAvroModel.getOrderId());
                PaymentResponse paymentResponse = orderMessagingDataMapper.paymentResponseAvroModelToPaymentResponse(paymentResponseAvroModel);
                paymentResponseMessageListener.paymentCancelled(paymentResponse);
            }
        });
    }
}
