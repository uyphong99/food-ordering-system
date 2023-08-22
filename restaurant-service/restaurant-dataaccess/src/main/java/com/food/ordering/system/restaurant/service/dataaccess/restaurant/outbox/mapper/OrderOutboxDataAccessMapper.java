package com.food.ordering.system.restaurant.service.dataaccess.restaurant.outbox.mapper;

import com.food.ordering.system.restaurant.service.dataaccess.restaurant.outbox.entity.OrderOutboxEntity;
import com.food.ordering.system.restaurant.service.domain.outbox.model.OrderOutboxMessage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderOutboxDataAccessMapper {
    public OrderOutboxEntity domainToEntity(OrderOutboxMessage orderOutboxMessage) {
        return OrderOutboxEntity.builder()
                .id(orderOutboxMessage.getId())
                .sagaId(orderOutboxMessage.getSagaId())
                .createdAt(orderOutboxMessage.getCreatedAt())
                .type(orderOutboxMessage.getType())
                .payload(orderOutboxMessage.getPayload())
                .outboxStatus(orderOutboxMessage.getOutboxStatus())
                .orderApprovalStatus(orderOutboxMessage.getOrderApprovalStatus())
                .version(orderOutboxMessage.getVersion())
                .build();
    }
    
    public OrderOutboxMessage entityToDomain(OrderOutboxEntity orderOutboxEntity) {
        return OrderOutboxMessage.builder()
                .id(orderOutboxEntity.getId())
                .sagaId(orderOutboxEntity.getSagaId())
                .createdAt(orderOutboxEntity.getCreatedAt())
                .type(orderOutboxEntity.getType())
                .orderApprovalStatus(orderOutboxEntity.getOrderApprovalStatus())
                .outboxStatus(orderOutboxEntity.getOutboxStatus())
                .payload(orderOutboxEntity.getPayload())
                .version(orderOutboxEntity.getVersion())
                .build();
    }
}
