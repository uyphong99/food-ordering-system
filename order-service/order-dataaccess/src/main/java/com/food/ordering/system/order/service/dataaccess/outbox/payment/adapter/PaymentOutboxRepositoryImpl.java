package com.food.ordering.system.order.service.dataaccess.outbox.payment.adapter;

import com.food.ordering.system.order.service.dataaccess.outbox.payment.entity.PaymentOutboxEntity;
import com.food.ordering.system.order.service.dataaccess.outbox.payment.exception.PaymentOutboxNotFoundException;
import com.food.ordering.system.order.service.dataaccess.outbox.payment.mapper.PaymentOutboxAccessMapper;
import com.food.ordering.system.order.service.dataaccess.outbox.payment.repository.PaymentOutboxJpaRepository;
import com.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.food.ordering.system.order.service.domain.ports.output.repository.PaymentOutboxRepository;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.saga.SagaStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class PaymentOutboxRepositoryImpl implements PaymentOutboxRepository {
    private final PaymentOutboxJpaRepository paymentOutboxJpaRepository;
    private final PaymentOutboxAccessMapper paymentOutboxAccessMapper;

    @Override
    public OrderPaymentOutboxMessage save(OrderPaymentOutboxMessage orderPaymentOutboxMessage) {
        return paymentOutboxAccessMapper.entityToDomain(
                paymentOutboxJpaRepository.save(paymentOutboxAccessMapper.domainToEntity(orderPaymentOutboxMessage))
        );
    }

    @Override
    public Optional<List<OrderPaymentOutboxMessage>> findByTypeAndOutboxStatusAndSagaStatuses(String type,
                                                                                              OutboxStatus outboxStatus,
                                                                                              SagaStatus... sagaStatuses) {

        return Optional.of(paymentOutboxJpaRepository.findAllByTypeAndOutboxStatusAndSagaStatusIn(type, outboxStatus, Arrays.stream(sagaStatuses).toList())
                        .orElseThrow(() -> new PaymentOutboxNotFoundException("Payment outbox object could not be " +
                                "found for saga type " + type))
                .stream()
                .map(paymentOutboxAccessMapper::entityToDomain)
                .toList());
    }

    @Override
    public Optional<OrderPaymentOutboxMessage> findByTypeAndSagaIdAndSagaStatuses(String type,
                                                                                  UUID sagaId,
                                                                                  SagaStatus... sagaStatuses) {
        return paymentOutboxJpaRepository.findByTypeAndSagaIdAndSagaStatusIn(type, sagaId, Arrays.stream(sagaStatuses).toList())
                .map(paymentOutboxAccessMapper::entityToDomain);
    }

    @Override
    public void deleteByTypeAndOutboxStatusAndSagaStatuses(String type, OutboxStatus outboxStatus, SagaStatus... sagaStatuses) {
        paymentOutboxJpaRepository.deleteByTypeAndOutboxStatusAndSagaStatusIn(type, outboxStatus, Arrays.stream(sagaStatuses).toList());
    }
}
