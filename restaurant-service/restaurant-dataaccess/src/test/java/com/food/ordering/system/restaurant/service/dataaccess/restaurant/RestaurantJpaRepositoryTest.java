package com.food.ordering.system.restaurant.service.dataaccess.restaurant;

import com.food.ordering.system.dataaccess.restaurant.entity.RestaurantEntity;
import com.food.ordering.system.dataaccess.restaurant.repository.RestaurantJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
@SpringBootTest(classes = RestaurantJpaRepository.class)
public class RestaurantJpaRepositoryTest {
    @MockBean
    private RestaurantJpaRepository repository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAllByRestaurantIdAndProductIdIn() {
        UUID restaurantId = UUID.randomUUID();
        List<UUID> productIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());

        RestaurantEntity entity1 = RestaurantEntity.builder()
                .restaurantId(restaurantId)
                .productId(productIds.get(0))
                .restaurantName("Restaurant 1")
                .restaurantActive(true)
                .productName("Product 1")
                .productPrice(BigDecimal.valueOf(10.0))
                .build();

        RestaurantEntity entity2 = RestaurantEntity.builder()
                .restaurantId(restaurantId)
                .productId(productIds.get(1))
                .restaurantName("Restaurant 2")
                .restaurantActive(false)
                .productName("Product 2")
                .productPrice(BigDecimal.valueOf(15.0))
                .build();

        List<RestaurantEntity> entities = Arrays.asList(entity1, entity2);

        when(repository.findAllByRestaurantIdAndProductIdIn(restaurantId, productIds))
                .thenReturn(Optional.of(entities));

        Optional<List<RestaurantEntity>> result = repository.findAllByRestaurantIdAndProductIdIn(restaurantId, productIds);

        assertEquals(entities, result.orElse(null));
    }
}
