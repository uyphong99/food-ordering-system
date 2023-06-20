package com.food.ordering.system.service.domain.unit;

import com.food.ordering.system.domain.valueobject.PaymentOrderStatus;
import com.food.ordering.system.payment.service.domain.PaymentRequestHelper;
import com.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.mapper.PaymentDataMapper;
import org.junit.jupiter.api.BeforeEach;

import java.util.UUID;


public class PaymentRequestHelperTest {
    private PaymentRequestHelper paymentRequestHelper;
    private PaymentRequest paymentRequestWithNullPrice;
    private PaymentDataMapper paymentDataMapper;

    @BeforeEach
    void setUp() {
        paymentRequestWithNullPrice = PaymentRequest.builder()
                .id(UUID.randomUUID().toString())
                .price(null)
                .orderId(UUID.randomUUID().toString())
                .paymentOrderStatus(PaymentOrderStatus.PENDING)
                .customerId(UUID.randomUUID().toString())
                .build();
    }

    void testPersistPayment_whenPriceIsNull() {

    }
}
