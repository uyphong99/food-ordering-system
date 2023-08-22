package com.food.ordering.system.restaurant.service.domain.event;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.restaurant.service.domain.entity.OrderApproval;

import java.time.ZonedDateTime;
import java.util.List;

public class OrderRejectedEvent extends OrderApprovalEvent{

    public OrderRejectedEvent(OrderApproval orderApproval,
                              RestaurantId restaurantId, List<String> failureMessage,
                              ZonedDateTime createdAt) {
        super(orderApproval, restaurantId, failureMessage, createdAt);
    }
}
