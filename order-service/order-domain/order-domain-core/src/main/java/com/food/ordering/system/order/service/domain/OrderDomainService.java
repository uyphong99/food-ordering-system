package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;

import java.util.List;
/**
 * [order-domain-core]
 * Response for operation relate with order domain.
 * - Method: validate init order, pay order, approve order, cancel order payment, cancel order.
 *
 * - Business logic that cannot fit in the aggregate. Used when multiple aggregates required in business logic.
 * Can interact with other domain services.
 * */
public interface OrderDomainService {

    /**
     * Check if the restaurant is active,
     * Update order products (map the information of restaurant products to order's products),
     * And create OrderCreatedEvent
     *
     * Return an OrderCreatedEvent
     * */
    OrderCreatedEvent  validateAndInitiateOrder(Order order, Restaurant restaurant);

    /**
     * Change order status to paid and return OrderPaidEvent
     *
     * */
    OrderPaidEvent payOrder(Order order);

    void approveOrder(Order order);

    OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessage);

    void cancelOrder(Order order, List<String> failureMessage);
}
