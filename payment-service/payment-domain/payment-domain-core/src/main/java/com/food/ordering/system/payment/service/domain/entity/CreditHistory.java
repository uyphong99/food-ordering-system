package com.food.ordering.system.payment.service.domain.entity;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.payment.service.domain.valueobject.CreditHistoryId;
import com.food.ordering.system.payment.service.domain.valueobject.TransactionType;

import java.util.UUID;

public class CreditHistory extends BaseEntity<CreditHistoryId> {
    private final CustomerId customerId;
    private final Money amount;
    private final TransactionType transactionType;

    private CreditHistory(Builder builder) {
        setId(builder.creditHistoryId);
        customerId = builder.customerId;
        amount = builder.amount;
        transactionType = builder.transactionType;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public Money getAmount() {
        return amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private CreditHistoryId creditHistoryId;
        private CustomerId customerId;
        private Money amount;
        private TransactionType transactionType;

        public Builder creditHistoryId(CreditHistoryId val) {
            this.creditHistoryId = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            this.customerId = val;
            return this;
        }

        public Builder money(Money val) {
            this.amount = val;
            return this;
        }

        public Builder transactionType(TransactionType val) {
            this.transactionType = transactionType;
            return this;
        }

        public CreditHistory build() {
            return new CreditHistory(this);
        }
    }
}
