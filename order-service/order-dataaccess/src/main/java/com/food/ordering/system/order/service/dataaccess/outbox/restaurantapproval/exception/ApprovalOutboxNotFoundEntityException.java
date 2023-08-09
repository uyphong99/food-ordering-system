package com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.exception;

public class ApprovalOutboxNotFoundEntityException extends RuntimeException {
    public ApprovalOutboxNotFoundEntityException(String message) {
        super(message);
    }
}
