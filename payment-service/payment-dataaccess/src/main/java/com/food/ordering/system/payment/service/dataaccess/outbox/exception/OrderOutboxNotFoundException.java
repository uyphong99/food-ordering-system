package com.food.ordering.system.payment.service.dataaccess.outbox.exception;

import com.food.ordering.system.payment.service.dataaccess.outbox.entity.OrderOutboxEntity;

public class OrderOutboxNotFoundException extends RuntimeException {
    public OrderOutboxNotFoundException(String message) {
        super(message);
    }
}
