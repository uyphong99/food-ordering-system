package com.food.ordering.system.customer.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueobject.CustomerId;
import lombok.Builder;


public class Customer extends AggregateRoot<CustomerId> {
    private final String username;
    private final String firstName;
    private final String lastName;

    public Customer(String username, String firstName, String lasgName) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lasgName;
    }

    private Customer(Builder builder) {
        super.setId(builder.id);
        username = builder.username;
        firstName = builder.firstName;
        lastName = builder.lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private CustomerId id;
        private String username;
        private String firstName;
        private String lastName;

        public Builder(String username, String firstName, String lasgName) {
            this.username = username;
            this.firstName = firstName;
            this.lastName = lasgName;
        }

        public Builder() {}

        public Builder customerId(CustomerId val) {
            id = val;
            return this;
        }

        public Builder username(String val) {
            username = val;
            return this;
        }

        public Builder firstName(String val) {
            firstName = val;
            return this;
        }

        public Builder lastName(String val) {
            lastName = val;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }
}
