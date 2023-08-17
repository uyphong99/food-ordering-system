package com.food.ordering.system.payment.service.dataaccess.outbox.mapper;

import com.food.ordering.system.payment.service.dataaccess.outbox.entity.OrderOutboxEntity;
import com.food.ordering.system.payment.service.domain.outbox.model.OrderOutboxMessage;
import org.springframework.stereotype.Component;

@Component
public class OrderOutboxDataAccessMapper {
    public OrderOutboxEntity domainToEntity(OrderOutboxMessage outboxMessage) {
        return OrderOutboxEntity.builder()
                .id(outboxMessage.getId())
                .sagaId(outboxMessage.getId())
                .createdAt(outboxMessage.getCreatedAt())
                .type(outboxMessage.getType())
                .payload(outboxMessage.getPayload())
                .outboxStatus(outboxMessage.getOutboxStatus())
                .paymentStatus(outboxMessage.getPaymentStatus())
                .version(outboxMessage.getVersion())
                .build();
    }

    public OrderOutboxMessage entityToDomain(OrderOutboxEntity orderOutboxEntity) {
        return OrderOutboxMessage.builder()
                .id(orderOutboxEntity.getId())
                .sagaId(orderOutboxEntity.getSagaId())
                .createdAt(orderOutboxEntity.getCreatedAt())
                .type(orderOutboxEntity.getType())
                .payload(orderOutboxEntity.getPayload())
                .outboxStatus(orderOutboxEntity.getOutboxStatus())
                .paymentStatus(orderOutboxEntity.getPaymentStatus())
                .version(orderOutboxEntity.getVersion())
                .build();
    }
}
