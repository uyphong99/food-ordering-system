package com.food.ordering.system.payment.service.domain.ports.output.message.publisher;

import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.payment.service.domain.outbox.model.OrderOutboxMessage;

import java.util.function.BiConsumer;

public interface PaymentResponseMessagePublisher {
    void publish(OrderOutboxMessage outboxMessage, BiConsumer<OrderOutboxMessage, OutboxStatus> outboxCallback);
}
