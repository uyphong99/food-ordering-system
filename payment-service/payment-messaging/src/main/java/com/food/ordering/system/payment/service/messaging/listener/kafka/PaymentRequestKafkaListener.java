package com.food.ordering.system.payment.service.messaging.listener.kafka;

import com.food.ordering.system.domain.valueobject.OrderApprovalStatus;
import com.food.ordering.system.domain.valueobject.PaymentOrderStatus;
import com.food.ordering.system.domain.valueobject.PaymentStatus;
import com.food.ordering.system.kafka.consumer.KafkaConsumer;
import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.entity.Payment;
import com.food.ordering.system.payment.service.domain.ports.input.message.listener.PaymentRequestMessageListener;
import com.food.ordering.system.payment.service.messaging.mapper.PaymentMessagingDataMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;
@Slf4j
@Component
@AllArgsConstructor
public class PaymentRequestKafkaListener implements KafkaConsumer<PaymentRequestAvroModel> {

    private final PaymentRequestMessageListener paymentRequestMessageListener;
    private final PaymentMessagingDataMapper paymentMessagingDataMapper;
    @Override
    @KafkaListener(id = "${kafka-consumer-config.payment-consumer-group-id}", topics = "${payment-service.payment-request-topic-name}")
    public void receive(@Payload List<PaymentRequestAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("{} number of payment responses received with keys: {}, partitions: {} and offsets: {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());

        messages.forEach(paymentRequestAvroModel -> {
            String paymentOrderStatus = paymentRequestAvroModel.getPaymentOrderStatus().toString();
            if (PaymentOrderStatus.PENDING.toString().equals(paymentOrderStatus)) {
                log.info("Processing payment for order id: {}", paymentRequestAvroModel.getOrderId());
                PaymentRequest paymentRequest = paymentMessagingDataMapper.paymentAvroModelToPaymentRequest(paymentRequestAvroModel);
                paymentRequestMessageListener.completePayment(paymentRequest);
            } else if (PaymentOrderStatus.CANCELLED.toString().equals(paymentOrderStatus)) {
                log.info("Cancelling payment for order id: {}", paymentRequestAvroModel.getOrderId());
                PaymentRequest paymentRequest = paymentMessagingDataMapper.paymentAvroModelToPaymentRequest(paymentRequestAvroModel);
                paymentRequestMessageListener.cancelPayment(paymentRequest);
            }
        });

    }
}
