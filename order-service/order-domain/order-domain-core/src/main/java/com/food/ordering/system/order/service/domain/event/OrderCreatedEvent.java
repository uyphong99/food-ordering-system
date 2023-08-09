package com.food.ordering.system.order.service.domain.event;

/**
 * This event will be published to the payment-request topic.
 * */
import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.order.service.domain.entity.Order;


import java.time.ZonedDateTime;
public class OrderCreatedEvent extends OrderEvent {
    public OrderCreatedEvent(Order order, ZonedDateTime createdAt) {
        super(order, createdAt);
    }
}

