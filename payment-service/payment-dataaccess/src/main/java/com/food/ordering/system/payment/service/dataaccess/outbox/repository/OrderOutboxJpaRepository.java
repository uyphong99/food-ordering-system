package com.food.ordering.system.payment.service.dataaccess.outbox.repository;

import com.food.ordering.system.domain.valueobject.PaymentStatus;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.payment.service.dataaccess.outbox.entity.OrderOutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderOutboxJpaRepository extends JpaRepository<OrderOutboxEntity, UUID> {
    Optional<List<OrderOutboxEntity>> findByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus);

    Optional<OrderOutboxEntity> findByTypeAndSagaIdAndPaymentStatusAndOutboxStatus(String type,
                                                                                   UUID sagaId,
                                                                                   PaymentStatus paymentStatus,
                                                                                   OutboxStatus outboxStatus);

    void deleteByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus);
}
