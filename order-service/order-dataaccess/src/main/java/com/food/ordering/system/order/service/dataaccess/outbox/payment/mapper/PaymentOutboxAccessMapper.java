package com.food.ordering.system.order.service.dataaccess.outbox.payment.mapper;

import com.food.ordering.system.order.service.dataaccess.outbox.payment.entity.PaymentOutboxEntity;
import com.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import org.springframework.stereotype.Component;

@Component
public class PaymentOutboxAccessMapper {
    public PaymentOutboxEntity domainToEntity(OrderPaymentOutboxMessage orderPaymentOutboxMessage) {
        return PaymentOutboxEntity.builder()
                .id(orderPaymentOutboxMessage.getId())
                .sagaId(orderPaymentOutboxMessage.getSagaId())
                .createdAt(orderPaymentOutboxMessage.getCreatedAt())
                .type(orderPaymentOutboxMessage.getType())
                .payload(orderPaymentOutboxMessage.getPayload())
                .orderStatus(orderPaymentOutboxMessage.getOrderStatus())
                .outboxStatus(orderPaymentOutboxMessage.getOutboxStatus())
                .sagaStatus(orderPaymentOutboxMessage.getSagaStatus())
                .version(orderPaymentOutboxMessage.getVersion())
                .build();
    }

    public OrderPaymentOutboxMessage entityToDomain(PaymentOutboxEntity paymentOutboxEntity) {
        return OrderPaymentOutboxMessage.builder()
                .id(paymentOutboxEntity.getId())
                .sagaId(paymentOutboxEntity.getSagaId())
                .createdAt(paymentOutboxEntity.getCreatedAt())
                .type(paymentOutboxEntity.getType())
                .payload(paymentOutboxEntity.getPayload())
                .orderStatus(paymentOutboxEntity.getOrderStatus())
                .outboxStatus(paymentOutboxEntity.getOutboxStatus())
                .sagaStatus(paymentOutboxEntity.getSagaStatus())
                .version(paymentOutboxEntity.getVersion())
                .build();
    }
}
