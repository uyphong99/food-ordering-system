package com.food.ordering.system.customer.service.domain.mapper;

import com.food.ordering.system.customer.service.domain.create.CreateCustomerCommand;
import com.food.ordering.system.customer.service.domain.create.CreateCustomerResponse;
import com.food.ordering.system.customer.service.domain.entity.Customer;
import com.food.ordering.system.domain.valueobject.CustomerId;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataMapper {
    public Customer dtoToDomain(CreateCustomerCommand createCustomerCommand) {
        return Customer.builder()
                .customerId(new CustomerId(createCustomerCommand.getCustomerId()))
                .lastName(createCustomerCommand.getLastName())
                .firstName(createCustomerCommand.getFistName())
                .username(createCustomerCommand.getUsername())
                .build();
    }

    public CreateCustomerResponse domainToDto(Customer customer, String message) {
        return new CreateCustomerResponse(customer.getId().getValue(), message);
    }
}
