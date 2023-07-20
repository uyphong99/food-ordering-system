package com.food.ordering.system.payment.service.domain.mapper;

import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.OrderId;
import com.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.entity.Payment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Component
public class PaymentDataMapper {
    public Payment paymentRequestToPayment(PaymentRequest paymentRequest) {
        Objects.requireNonNull(paymentRequest, "paymentRequest must not be null");
        Objects.requireNonNull(paymentRequest.getOrderId(), "orderId must not be null");
        Objects.requireNonNull(paymentRequest.getCustomerId(), "customerId must not be null");
        Objects.requireNonNull(paymentRequest.getPrice(), "price must not be null");

        UUID orderId = UUID.fromString(paymentRequest.getOrderId());
        UUID customerId = UUID.fromString(paymentRequest.getCustomerId());
        BigDecimal price = paymentRequest.getPrice();

        return Payment.builder()
                .orderId(new OrderId(orderId))
                .customerId(new CustomerId(customerId))
                .price(new Money(price))
                .build();
    }
}
