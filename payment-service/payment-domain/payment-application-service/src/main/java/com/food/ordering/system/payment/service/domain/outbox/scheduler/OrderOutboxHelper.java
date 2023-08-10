package com.food.ordering.system.payment.service.domain.outbox.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.ordering.system.domain.DomainConstants;
import com.food.ordering.system.domain.valueobject.PaymentStatus;
import com.food.ordering.system.outbox.OutboxScheduler;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.payment.service.domain.exception.PaymentDomainException;
import com.food.ordering.system.payment.service.domain.outbox.model.OrderEventPayload;
import com.food.ordering.system.payment.service.domain.outbox.model.OrderOutboxMessage;
import com.food.ordering.system.payment.service.domain.ports.output.repository.OrderOutboxRepository;
import com.food.ordering.system.saga.order.SagaConstants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@AllArgsConstructor
public class OrderOutboxHelper {
    private final OrderOutboxRepository orderOutboxRepository;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public Optional<OrderOutboxMessage>  getCompletedOrderOutboxMessageBySagaIdAndPaymentStatus(UUID sagaId,
                                                                                                PaymentStatus paymentStatus) {
        return orderOutboxRepository.findByTypeAndSagaIdAndPaymentStatusAndOutboxStatus(SagaConstants.ORDER_SAGA_NAME, sagaId,
                paymentStatus, OutboxStatus.COMPLETED);
    }

    @Transactional(readOnly = true)
    public Optional<List<OrderOutboxMessage>> getOrderOutboxMessageByOutboxStatus(OutboxStatus outboxStatus) {
        return orderOutboxRepository.findByTypeAndOutboxStatus(SagaConstants.ORDER_SAGA_NAME, outboxStatus);
    }

    @Transactional
    public void deleteOrderOutboxMessageByOutboxStatus(OutboxStatus outboxStatus) {
        orderOutboxRepository.deleteByTypeAndOutboxStatus(SagaConstants.ORDER_SAGA_NAME, outboxStatus);
    }

    public void saveOrderOutboxMessage(OrderEventPayload orderEventPayload,
                                       PaymentStatus paymentStatus,
                                       OutboxStatus outboxStatus,
                                       UUID sagaId) {
        save(OrderOutboxMessage.builder()
                .id(UUID.randomUUID())
                .sagaId(sagaId)
                .createdAt(orderEventPayload.getCreatedAt())
                .processedAt(ZonedDateTime.now(ZoneId.of(DomainConstants.UTC)))
                .type(SagaConstants.ORDER_SAGA_NAME)
                .payload(createPayload(orderEventPayload))
                .paymentStatus(paymentStatus)
                .outboxStatus(outboxStatus)
                .build());
    }
    @Transactional
    public void updateOutboxMessage(OrderOutboxMessage outboxMessage, OutboxStatus outboxStatus) {
        outboxMessage.setOutboxStatus(outboxStatus);
        save(outboxMessage);
        log.info("Order outbox table status is updated as: {}", outboxStatus.name());
    }

    private String createPayload(OrderEventPayload orderEventPayload) {
        try {
            return objectMapper.writeValueAsString(orderEventPayload);
        } catch (JsonProcessingException e) {
            log.error("Could not create OrderEventPayload json!", e);
            throw new PaymentDomainException("Could not create OrderEventPayload json!", e);
        }
    }

    private void save(OrderOutboxMessage outboxMessage) {
        OrderOutboxMessage response = orderOutboxRepository.save(outboxMessage);
        if (response == null) {
            log.error("Could not save OrderOutboxMessage!");
            throw new PaymentDomainException("Could not save OrderOutboxMessage!");
        }
    }
}
