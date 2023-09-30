package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueobject.CustomerId;
import lombok.Builder;

public class Customer extends AggregateRoot<CustomerId> {
    private String username;
    private String firstName;
    private String lastName;
    public Customer() {}

    public Customer(CustomerId customerId, String firstName, String lastName, String username) {
        super.setId(customerId);
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    public Customer(CustomerId customerId) {
        super.setId(customerId);
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
}
