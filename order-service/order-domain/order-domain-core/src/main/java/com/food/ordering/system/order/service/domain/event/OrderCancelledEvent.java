package com.food.ordering.system.order.service.domain.event;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.order.service.domain.entity.Order;

import java.time.ZonedDateTime;

public class OrderCancelledEvent extends OrderEvent {

    private final DomainEventPublisher<OrderCancelledEvent> orderCancelledEventPublisher;
    public OrderCancelledEvent(Order order, ZonedDateTime createdAt, DomainEventPublisher<OrderCancelledEvent> orderCancelledEventPublisher) {
        super(order, createdAt);
        this.orderCancelledEventPublisher = orderCancelledEventPublisher;
    }

    @Override
    public void fire() {
        orderCancelledEventPublisher.publish(this);
    }
}
