package com.food.ordering.system.customer.service.domain;

import com.food.ordering.system.customer.service.domain.entity.Customer;
import com.food.ordering.system.customer.service.domain.event.CustomerCreatedEvent;
import com.food.ordering.system.domain.DomainConstants;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Slf4j
public class CustomerDomainServiceImpl implements CustomerDomainService{
    @Override
    public CustomerCreatedEvent validateAndInitiateCustomer(Customer customer) {
        log.info("Customer with id: {}", customer.getId().getValue());
        return new CustomerCreatedEvent(customer, ZonedDateTime.now(ZoneId.of(DomainConstants.UTC)));
    }
}
