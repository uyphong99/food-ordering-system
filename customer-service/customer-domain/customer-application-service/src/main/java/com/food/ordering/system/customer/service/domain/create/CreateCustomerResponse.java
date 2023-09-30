package com.food.ordering.system.customer.service.domain.create;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class CreateCustomerResponse {
    @NotNull
    private final UUID customerId;
    @NotNull
    private final String message;

    public CreateCustomerResponse(UUID customerId, String message) {
        this.customerId = customerId;
        this.message = message;
    }
}
