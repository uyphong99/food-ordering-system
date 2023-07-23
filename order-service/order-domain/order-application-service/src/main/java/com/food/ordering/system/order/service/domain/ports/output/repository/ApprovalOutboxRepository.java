package com.food.ordering.system.order.service.domain.ports.output.repository;

import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.saga.SagaStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApprovalOutboxRepository {
    OrderApprovalOutboxMessage save(OrderApprovalOutboxMessage orderApprovalOutboxMessage);

    Optional<List<OrderApprovalOutboxMessage>> findByTypeAndOutboxStatusAndSagaStatuses(String type,
                                                                                       OutboxStatus outboxStatus,
                                                                                       SagaStatus... sagaStatuses);
    Optional<OrderApprovalOutboxMessage> findByTypeAndSagaIdAndSagaStatuses(String type,
                                                                           UUID sagaId,
                                                                           SagaStatus... sagaStatuses);

    void deleteByTypeAndOutboxStatusAndSagaStatuses(String type,
                                                    OutboxStatus outboxStatus,
                                                    SagaStatus... sagaStatuses);
}
