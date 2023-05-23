package com.food.ordering.system.order.service.domain.dto.create;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
/**
 * Represents a command to create a new order. This command contains all the information needed to create an order,
 * including the customer ID, restaurant ID, price, order items, and delivery address.
 */
@Getter
@Builder
@AllArgsConstructor
public class CreateOrderCommand {
    @NotNull
    private UUID customerId;
    @NotNull
    private UUID restaurantId;
    @NotNull
    private BigDecimal price;
    @NotNull
    private List<OrderItem> items;
    @NotNull
    private OrderAddress address;
}
