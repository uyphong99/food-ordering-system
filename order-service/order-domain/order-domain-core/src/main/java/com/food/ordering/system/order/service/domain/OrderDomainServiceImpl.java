package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService{
    private static final String UTC = "UTC";

    /**
     * valid the restaurant, update order products, and create CreatedEvent
     * */
    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant) {
        validateRestaurant(restaurant);
        setOrderProductInformation(order, restaurant);
        order.validateOrder();
        order.initializeOrder();
        log.info(String.format("Order with id: " + order.getId().getValue()) + " is initiated");
        return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }



    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.pay();
        log.info("Order with ID: " + order.getId().getValue() + " is paid");
        return new OrderPaidEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void approveOrder(Order order) {
        order.approve();
        log.info("Order with id: " + order.getId().getValue() + " is approved");
    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessage) {
        order.initCancel(failureMessage);
        log.info("Order payment is cancelling for order id: " + order.getId().getValue());
        return new OrderCancelledEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessage) {
        order.cancel(failureMessage);
        log.info("Order with ID: " + order.getId().getValue() + " is cancelled");
    }

    /**
     * Check if restaurant is active, if not throw OrderDomainException
     * */
    private void validateRestaurant(Restaurant restaurant) {
        if (!restaurant.isActive()) {
            throw new OrderDomainException("Restaurant with id " + restaurant.getId().getValue() +
                    " is currently not active!");
        }
    }

    /**
     * Update the order's products to have information of the restaurant's products (They have same ID)
     * */
    private void setOrderProductInformation(Order order, Restaurant restaurant) {
        order.getItems().forEach(orderItem -> restaurant.getProducts().forEach( restaurantProduct -> {
            Product currentProduct = orderItem.getProduct();
            if ( currentProduct.equals(restaurantProduct)) {
                currentProduct.updateWithConfirmedNameAndPrice(restaurantProduct.getName(), restaurantProduct.getPrice());
            }
        }));
    }
}
