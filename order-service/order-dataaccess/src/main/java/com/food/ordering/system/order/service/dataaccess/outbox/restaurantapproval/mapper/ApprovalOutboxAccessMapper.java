package com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.mapper;

import com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.entity.ApprovalOutboxEntity;
import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import org.springframework.stereotype.Component;

@Component
public class ApprovalOutboxAccessMapper {

    public ApprovalOutboxEntity domainToEntity(OrderApprovalOutboxMessage orderApprovalOutboxMessage) {
        return ApprovalOutboxEntity.builder()
                .id(orderApprovalOutboxMessage.getId())
                .sagaId(orderApprovalOutboxMessage.getSagaId())
                .createdAt(orderApprovalOutboxMessage.getCreatedAt())
                .type(orderApprovalOutboxMessage.getType())
                .payload(orderApprovalOutboxMessage.getPayload())
                .sagaStatus(orderApprovalOutboxMessage.getSagaStatus())
                .outboxStatus(orderApprovalOutboxMessage.getOutboxStatus())
                .orderStatus(orderApprovalOutboxMessage.getOrderStatus())
                .version(orderApprovalOutboxMessage.getVersion())
                .build();
    }

    public OrderApprovalOutboxMessage entityToDomain(ApprovalOutboxEntity approvalOutboxEntity) {
        return OrderApprovalOutboxMessage.builder()
                .id(approvalOutboxEntity.getId())
                .sagaId(approvalOutboxEntity.getSagaId())
                .type(approvalOutboxEntity.getType())
                .createdAt(approvalOutboxEntity.getCreatedAt())
                .orderStatus(approvalOutboxEntity.getOrderStatus())
                .outboxStatus(approvalOutboxEntity.getOutboxStatus())
                .sagaStatus(approvalOutboxEntity.getSagaStatus())
                .payload(approvalOutboxEntity.getPayload())
                .version(approvalOutboxEntity.getVersion())
                .build();
    }
}
