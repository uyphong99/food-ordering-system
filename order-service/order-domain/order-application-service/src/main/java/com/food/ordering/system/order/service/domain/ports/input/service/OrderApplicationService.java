package com.food.ordering.system.order.service.domain.ports.input.service;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;
import jakarta.validation.Valid;

/**
 * An interface (port) has two method: createOrder and trackOrder.
 * Will be used by OrderController in order-application module.
 *
 * Allows the isolated domain to communicate with outside. Orchestrate transactions,
 * security, looking up proper aggregates and saving state changes of the domain to the database.
 * Doesn't contain any business logic.
 * */
public interface OrderApplicationService {

    /**
     * Persist order to DB, publish event and return CreateOrderResponse.
     * */
    CreateOrderResponse createOrder(@Valid CreateOrderCommand createOrderCommand);

    TrackOrderResponse trackOrder(@Valid TrackOrderQuery trackOrderQuery);
}
