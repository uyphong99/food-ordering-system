package com.food.ordering.system.restaurant.service.domain.outbox.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.ordering.system.domain.DomainConstants;
import com.food.ordering.system.domain.valueobject.OrderApprovalStatus;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.restaurant.service.domain.exception.RestaurantApplicationServiceException;
import com.food.ordering.system.restaurant.service.domain.outbox.model.OrderEventPayload;
import com.food.ordering.system.restaurant.service.domain.outbox.model.OrderOutboxMessage;
import com.food.ordering.system.restaurant.service.domain.ports.output.repository.OrderOutboxRepository;
import com.food.ordering.system.saga.order.SagaConstants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Slf4j
@AllArgsConstructor
public class OrderOutboxHelper {
    private final OrderOutboxRepository orderOutboxRepository;
    private final ObjectMapper objectMapper;
    @Transactional
    public void saveOrderOutboxMessage(OrderEventPayload orderEventPayload,
                                       UUID sagaId,
                                       OrderApprovalStatus orderApprovalStatus,
                                       OutboxStatus outboxStatus) {
        orderOutboxRepository.save(OrderOutboxMessage.builder()
                        .id(UUID.randomUUID())
                        .sagaId(sagaId)
                        .orderApprovalStatus(OrderApprovalStatus.valueOf(orderEventPayload.getOrderApprovalStatus()))
                        .outboxStatus(outboxStatus)
                        .orderApprovalStatus(orderApprovalStatus)
                        .type(SagaConstants.ORDER_SAGA_NAME)
                        .createdAt(ZonedDateTime.now(ZoneId.of(DomainConstants.UTC)))
                        .payload(createPayloadString(orderEventPayload))
                .build());
    }
    @Transactional(readOnly = true)
    public Optional<List<OrderOutboxMessage>> findByOutboxStatus(OutboxStatus outboxStatus) {
        return orderOutboxRepository.findBySagaTypeAndOutboxStatus(SagaConstants.ORDER_SAGA_NAME, outboxStatus);
    }

    @Transactional
    public void updateOutboxMessage(OrderOutboxMessage outboxMessage, OutboxStatus outboxStatus) {
        outboxMessage.setOutboxStatus(outboxStatus);
        save(outboxMessage);
        log.info("Order outbox table status is updated as: {}", outboxStatus.name());
    }
    @Transactional
    public void deleteOrderOutboxMessageByOutboxStatus(OutboxStatus outboxStatus) {
        orderOutboxRepository.deleteBySagaTypeAndOutboxStatus(SagaConstants.ORDER_SAGA_NAME, outboxStatus);
    }

    private void save(OrderOutboxMessage outboxMessage) {
        OrderOutboxMessage orderOutboxMessage = orderOutboxRepository.save(outboxMessage);

        if (orderOutboxMessage == null) {
            log.error("Could not save OrderOutboxMessage!");
            throw new RestaurantApplicationServiceException("Could not save OrderOutboxMessage!");
        }
    }

    private String createPayloadString(OrderEventPayload orderEventPayload) {
        try {
            return objectMapper.writeValueAsString(orderEventPayload);
        } catch (JsonProcessingException e) {
            log.error("Could not create OrderEventPayload json!", e);
            throw new RestaurantApplicationServiceException("Could not create OrderEventPayload json!", e);
        }
    }
}
