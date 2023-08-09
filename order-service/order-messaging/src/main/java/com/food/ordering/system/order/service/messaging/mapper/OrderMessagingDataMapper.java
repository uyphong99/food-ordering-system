package com.food.ordering.system.order.service.messaging.mapper;

import com.food.ordering.system.kafka.order.avro.model.*;
import com.food.ordering.system.order.service.domain.dto.message.PaymentResponse;
import com.food.ordering.system.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalEventPayload;
import com.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentEventPayload;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Map dto in domain core to avro model and vice versa.
 * */
@Component
public class OrderMessagingDataMapper {

    public PaymentResponse paymentResponseAvroModelToPaymentResponse(PaymentResponseAvroModel paymentResponseAvroModel) {
        return PaymentResponse.builder()
                .id(paymentResponseAvroModel.getId())
                .orderId(paymentResponseAvroModel.getOrderId())
                .paymentId(paymentResponseAvroModel.getPaymentId())
                .paymentStatus(com.food.ordering.system.domain.valueobject.PaymentStatus.valueOf(paymentResponseAvroModel.getPaymentStatus().name()))
                .price(paymentResponseAvroModel.getPrice())
                .createdAt(paymentResponseAvroModel.getCreatedAt())
                .sagaId(paymentResponseAvroModel.getSagaId())
                .failureMessage(paymentResponseAvroModel.getFailureMessages())
                .customerId(paymentResponseAvroModel.getCustomerId())
                .build();
    }


    public RestaurantApprovalResponse restaurantResponseAvroModelToRestaurantResponse(RestaurantApprovalResponseAvroModel restaurantResponseAvroModel) {
        return RestaurantApprovalResponse.builder()
                .restaurantId(restaurantResponseAvroModel.getRestaurantId())
                .orderApprovalStatus(
                        com.food.ordering.system.domain.valueobject.OrderApprovalStatus.valueOf(
                                restaurantResponseAvroModel.getOrderApprovalStatus().toString()))
                .orderId(restaurantResponseAvroModel.getOrderId())
                .createdAt(restaurantResponseAvroModel.getCreatedAt())
                .sagaId(restaurantResponseAvroModel.getSagaId())
                .failureMessages(restaurantResponseAvroModel.getFailureMessages())
                .build();
    }

    public PaymentRequestAvroModel paymentEventToPaymentRequestAvroModel(String sagaId,
                                                                         OrderPaymentEventPayload orderPaymentEventPayload) {
        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId(sagaId)
                .setCustomerId(orderPaymentEventPayload.getCustomerId())
                .setPrice(orderPaymentEventPayload.getPrice())
                .setCreatedAt(orderPaymentEventPayload.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.valueOf(orderPaymentEventPayload.getPaymentOrderStatus()))
                .build();
    }

    public RestaurantApprovalRequestAvroModel approvalEventToApprovalRequestAvroModel(String sagaId,
                                                                                      OrderApprovalEventPayload orderApprovalEventPayload) {
        return RestaurantApprovalRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId(sagaId)
                .setRestaurantId(orderApprovalEventPayload.getRestaurantId())
                .setOrderId(orderApprovalEventPayload.getOrderId())
                .setRestaurantId(orderApprovalEventPayload.getRestaurantId())
                .setRestaurantOrderStatus(RestaurantOrderStatus.valueOf(orderApprovalEventPayload.getRestaurantOrderStatus()))
                .setProducts(orderApprovalEventPayload.getProducts().stream().map(orderApprovalEventProduct ->
                        com.food.ordering.system.kafka.order.avro.model.Product.newBuilder()
                                .setId(orderApprovalEventProduct.getId().toString())
                                .setQuantity(orderApprovalEventProduct.getQuantity()
                                        ).build()).collect(Collectors.toList()))
                .setPrice(orderApprovalEventPayload.getPrice())
                .setCreatedAt(orderApprovalEventPayload.getCreatedAt().toInstant())
                .build();
    }
}
