package com.food.ordering.system.customer.service.domain.create;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class CreateCustomerCommand {
    @NotNull
    private final UUID customerId;
    @NotNull
    private final String username;
    @NotNull
    private final String fistName;
    @NotNull
    private final String lastName;

    public CreateCustomerCommand(UUID customerId, String username, String fistName, String lastName) {
        this.customerId = customerId;
        this.username = username;
        this.fistName = fistName;
        this.lastName = lastName;
    }
}
