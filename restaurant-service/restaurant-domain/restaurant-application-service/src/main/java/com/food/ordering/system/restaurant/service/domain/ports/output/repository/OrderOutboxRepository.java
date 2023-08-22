package com.food.ordering.system.restaurant.service.domain.ports.output.repository;

import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.restaurant.service.domain.outbox.model.OrderOutboxMessage;

import java.util.List;
import java.util.Optional;

public interface OrderOutboxRepository {
    OrderOutboxMessage save(OrderOutboxMessage orderOutboxMessage);
    Optional<List<OrderOutboxMessage>> findBySagaTypeAndOutboxStatus(String sagaType, OutboxStatus outboxStatus);
    void deleteBySagaTypeAndOutboxStatus(String orderSagaName, OutboxStatus outboxStatus);
}
