package com.food.ordering.system.dataaccess.restaurant.entity;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RestaurantEntityId implements Serializable {
    private UUID restaurantId;
    private UUID productId;
}
