package com.food.ordering.system.payment.service.domain.event;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.payment.service.domain.entity.Payment;

import java.time.ZonedDateTime;
import java.util.Collections;

public class PaymentCancelledEvent extends PaymentEvent{
    private final DomainEventPublisher<PaymentCancelledEvent> paymentCancelledEventPublisher;
    public PaymentCancelledEvent(Payment payment, ZonedDateTime createdAt, DomainEventPublisher<PaymentCancelledEvent> paymentCancelledEventPublisher) {
        super(payment, createdAt, Collections.emptyList());
        this.paymentCancelledEventPublisher = paymentCancelledEventPublisher;
    }


    @Override
    public void fire() {
        paymentCancelledEventPublisher.publish(this);
    }
}
