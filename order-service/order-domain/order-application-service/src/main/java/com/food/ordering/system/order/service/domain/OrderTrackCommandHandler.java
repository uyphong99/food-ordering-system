package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.exception.OrderNotFoundException;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.service.domain.valueobject.TrackingId;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class OrderTrackCommandHandler {
    private final OrderDataMapper orderDataMapper;
    private final OrderRepository orderRepository;

    /**
     * Tracks an order based on the provided {@link TrackOrderQuery}. This method retrieves the order from the
     * {@link OrderRepository} using the tracking ID specified in the query. If the order is found, it is mapped
     * to a {@link TrackOrderResponse} using the {@link OrderDataMapper} and returned. If the order is not found,
     * an {@link OrderNotFoundException} is thrown
     *
     * @param trackOrderQuery the {@link TrackOrderQuery} containing the order tracking ID
     * @return a {@link TrackOrderResponse} object with the tracked order details
     * @throws OrderNotFoundException if the order with the specified tracking ID is not found
     */
    @Transactional(readOnly = true)
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
        Optional<Order> order = orderRepository
                .findByTrackingId(new TrackingId(trackOrderQuery.getOrderTrackingId()));

        if (!order.isPresent()) {
            log.warn("Could not find order with tracking id: " + trackOrderQuery.getOrderTrackingId());
            throw new OrderNotFoundException("Could not find order with tracking id: " + trackOrderQuery.getOrderTrackingId());
        }

        return orderDataMapper.orderToTrackOrderResponse(order.get());
    }
}
