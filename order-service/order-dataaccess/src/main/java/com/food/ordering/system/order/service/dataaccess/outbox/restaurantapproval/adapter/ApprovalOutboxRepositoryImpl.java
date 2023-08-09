package com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.adapter;

import com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.exception.ApprovalOutboxNotFoundEntityException;
import com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.mapper.ApprovalOutboxAccessMapper;
import com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.repository.ApprovalOutboxJpaRepository;
import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.food.ordering.system.order.service.domain.ports.output.repository.ApprovalOutboxRepository;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.saga.SagaStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class ApprovalOutboxRepositoryImpl implements ApprovalOutboxRepository {
    private final ApprovalOutboxJpaRepository approvalOutboxJpaRepository;
    private final ApprovalOutboxAccessMapper approvalOutboxAccessMapper;
    @Override
    public OrderApprovalOutboxMessage save(OrderApprovalOutboxMessage orderApprovalOutboxMessage) {
        return approvalOutboxAccessMapper.entityToDomain(
                approvalOutboxJpaRepository.save(approvalOutboxAccessMapper.domainToEntity(orderApprovalOutboxMessage)));
    }

    @Override
    public Optional<List<OrderApprovalOutboxMessage>> findByTypeAndOutboxStatusAndSagaStatuses(String type,
                                                                                               OutboxStatus outboxStatus,
                                                                                               SagaStatus... sagaStatuses) {
        return Optional.of(
                approvalOutboxJpaRepository.findByTypeAndOutboxStatusAndSagaStatusIn(type,
                                outboxStatus, Arrays.stream(sagaStatuses).toList())
                .orElseThrow(() -> new ApprovalOutboxNotFoundEntityException("Approval outbox " +
                        "cannot be found for saga type" + type))
                .stream()
                .map(approvalOutboxAccessMapper::entityToDomain)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<OrderApprovalOutboxMessage> findByTypeAndSagaIdAndSagaStatuses(String type, UUID sagaId, SagaStatus... sagaStatuses) {
        return approvalOutboxJpaRepository.findByTypeAndSagaIdAndSagaStatusIn(type, sagaId, Arrays.stream(sagaStatuses).toList())
                .map(approvalOutboxAccessMapper::entityToDomain);
    }

    @Override
    public void deleteByTypeAndOutboxStatusAndSagaStatuses(String type,
                                                           OutboxStatus outboxStatus,
                                                           SagaStatus... sagaStatuses) {
        approvalOutboxJpaRepository.deleteByTypeAndOutboxStatusAndSagaStatusIn(type,
                outboxStatus, Arrays.stream(sagaStatuses).toList());
    }
}
