package com.food.ordering.system.restaurant.service.domain.entity;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueobject.OrderApprovalStatus;
import com.food.ordering.system.domain.valueobject.OrderId;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.restaurant.service.domain.valueobject.OrderApprovalId;

public class OrderApproval extends BaseEntity<OrderApprovalId> {
    private final RestaurantId restaurantId;
    private final OrderId orderId;
    private final OrderApprovalStatus orderApprovalStatus;

    private OrderApproval(Builder builder) {
        id = builder.id;
        restaurantId = builder.restaurantId;
        orderId = builder.orderId;
        orderApprovalStatus = builder.orderApprovalStatus;
    }

    public RestaurantId getRestaurantId() {
        return restaurantId;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public OrderApprovalStatus getOrderApprovalStatus() {
        return orderApprovalStatus;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private ID id;
        private RestaurantId restaurantId;
        private OrderId orderId;
        private OrderApprovalStatus orderApprovalStatus;

        public Builder(RestaurantId restaurantId, OrderId orderId, OrderApprovalStatus orderApprovalStatus) {
            this.restaurantId = restaurantId;
            this.orderId = orderId;
            this.orderApprovalStatus = orderApprovalStatus;
        }

        public Builder id(ID val) {
            id = val;
            return this;
        }

        public OrderApproval build() {
            return new OrderApproval(this);
        }
    }
}
