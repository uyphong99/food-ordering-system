package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.domain.valueobject.OrderStatus;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;

import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentEventPayload;
import com.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.food.ordering.system.order.service.domain.outbox.scheduler.payment.PaymentOutboxHelper;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.payment.PaymentRequestMessagePublisher;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.saga.SagaStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Create order, save order to DB and trigger CreateOrderEvent event.
 * */
@Slf4j
@Component
@AllArgsConstructor
public class OrderCreateCommandHandler {
    private final OrderCreateHelper orderCreateHelper;
    private final OrderDataMapper orderDataMapper;
    private final PaymentOutboxHelper paymentOutboxHelper;
    private final OrderSagaHelper orderSagaHelper;
    /**
     * Creates a new order based on the provided {@link CreateOrderCommand}.
     * Publishes an order created event using the
     * {@link CreateOrderResponse} using the {@link OrderDataMapper} and returns the response indicating
     * a successful order creation.
     *
     * @param createOrderCommand the {@link CreateOrderCommand} containing the order details
     * @return a {@link CreateOrderResponse} object indicating a successful order creation
     */
    @Transactional
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        OrderCreatedEvent orderCreatedEvent = orderCreateHelper.persistOrder(createOrderCommand);
        log.info("Order is created with id: " + orderCreatedEvent.getOrder().getId().getValue());
        CreateOrderResponse createOrderResponse = orderDataMapper
                .orderToCreateOrderResponse(orderCreatedEvent.getOrder(), "Order Created Successfully");

        OrderPaymentEventPayload paymentEventPayload = orderDataMapper.orderEventToOrderEventPayload(orderCreatedEvent);
        OrderStatus orderStatus = orderCreatedEvent.getOrder().getOrderStatus();

        paymentOutboxHelper.savePaymentOutboxMessage(
                paymentEventPayload,
                orderStatus,
                orderSagaHelper.orderStatusToSagaStatus(orderStatus),
                OutboxStatus.STARTED,
                UUID.randomUUID());

        log.info("Returning CreateOrderResponse with order id: {} ", orderCreatedEvent.getOrder().getId());

        return createOrderResponse;
    }
}
