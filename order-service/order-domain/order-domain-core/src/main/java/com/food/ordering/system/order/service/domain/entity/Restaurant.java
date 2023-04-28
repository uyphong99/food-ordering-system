package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import lombok.Getter;

import java.util.List;
@Getter
public class Restaurant extends AggregateRoot<RestaurantId> {
    private final List<Product> products;
    private boolean active;

    private Restaurant(Builder builder) {
        super.setId(builder.restaurantId);
        products = builder.products;
        active = builder.active;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private RestaurantId restaurantId;
        private List<Product> products;
        private boolean active;

        public Builder() {}

        public Builder(List<Product> products) {
            this.products = products;
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder active(boolean val) {
            active = val;
            return this;
        }

        public Builder products(List<Product> val) {
            this.products = val;
            return this;
        }

        public Restaurant build() {
            return new Restaurant(this);
        }
    }
}
