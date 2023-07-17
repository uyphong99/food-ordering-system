package com.food.ordering.system.dataaccess.restaurant.repository;


import com.food.ordering.system.dataaccess.restaurant.entity.RestaurantEntity;
import com.food.ordering.system.dataaccess.restaurant.entity.RestaurantEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestaurantJpaRepository extends JpaRepository<RestaurantEntity, RestaurantEntityId> {
    @Query("SELECT r FROM RestaurantEntity r WHERE r.restaurantId = ?1 AND r.productId IN ?2")
    Optional<List<RestaurantEntity>> findAllByRestaurantIdAndProductIdIn(UUID restaurantId, List<UUID> productIds);
}
