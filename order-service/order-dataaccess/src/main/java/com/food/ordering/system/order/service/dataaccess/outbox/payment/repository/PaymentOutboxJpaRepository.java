package com.food.ordering.system.order.service.dataaccess.outbox.payment.repository;

import com.food.ordering.system.order.service.dataaccess.outbox.payment.entity.PaymentOutboxEntity;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.saga.SagaStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentOutboxJpaRepository extends JpaRepository<PaymentOutboxEntity, UUID> {
    Optional<List<PaymentOutboxEntity>> findAllByTypeAndOutboxStatusAndSagaStatusIn(String type,
                                                                                    OutboxStatus outboxStatus,
                                                                                    List<SagaStatus> sagaStatuses);

    Optional<PaymentOutboxEntity> findByTypeAndSagaIdAndSagaStatusIn(String type, UUID sagaId, List<SagaStatus> sagaStatuses);
    void deleteByTypeAndOutboxStatusAndSagaStatusIn(String type, OutboxStatus outboxStatus, List<SagaStatus> sagaStatuses);
}
