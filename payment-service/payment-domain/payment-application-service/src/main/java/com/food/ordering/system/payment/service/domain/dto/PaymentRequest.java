package com.food.ordering.system.payment.service.domain.dto;

import com.food.ordering.system.domain.valueobject.PaymentOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class PaymentRequest {
    private String id;
    private String sagaId;
    private String orderId;
    private String customerId;
    private BigDecimal price;
    private Instant createdAt;
    private PaymentOrderStatus paymentOrderStatus;

    private PaymentRequest(Builder builder) {
        setId(builder.id);
        setSagaId(builder.sagaId);
        setOrderId(builder.orderId);
        setCustomerId(builder.customerId);
        setPrice(builder.price);
        setCreatedAt(builder.createdAt);
        setPaymentOrderStatus(builder.paymentOrderStatus);
    }

    public void setPaymentOrderStatus(PaymentOrderStatus paymentOrderStatus) {
        this.paymentOrderStatus = paymentOrderStatus;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private String id;
        private String sagaId;
        private String orderId;
        private String customerId;
        private BigDecimal price;
        private Instant createdAt;
        private PaymentOrderStatus paymentOrderStatus;

        public Builder() {
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder sagaId(String val) {
            sagaId = val;
            return this;
        }

        public Builder orderId(String val) {
            orderId = val;
            return this;
        }

        public Builder customerId(String val) {
            customerId = val;
            return this;
        }

        public Builder price(BigDecimal val) {
            price = val;
            return this;
        }

        public Builder createdAt(Instant val) {
            createdAt = val;
            return this;
        }

        public Builder paymentOrderStatus(PaymentOrderStatus val) {
            paymentOrderStatus = val;
            return this;
        }

        public PaymentRequest build() {
            return new PaymentRequest(this);
        }
    }
}
