package com.food.ordering.system.restaurant.service.messaging.mapper;

import com.food.ordering.system.domain.valueobject.ProductId;
import com.food.ordering.system.domain.valueobject.RestaurantOrderStatus;
import com.food.ordering.system.kafka.order.avro.model.OrderApprovalStatus;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.ordering.system.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.food.ordering.system.restaurant.service.domain.entity.Product;
import com.food.ordering.system.restaurant.service.domain.event.OrderApprovalEvent;
import com.food.ordering.system.restaurant.service.domain.event.OrderApprovedEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RestaurantMessagingDataMapper {


    public RestaurantApprovalRequest approvalRequestAvroModelToApprovalRequest(RestaurantApprovalRequestAvroModel restaurantApprovalRequestAvroModel) {
        return RestaurantApprovalRequest.builder()
                .id(restaurantApprovalRequestAvroModel.getId())
                .restaurantId(restaurantApprovalRequestAvroModel.getRestaurantId())
                .orderId(restaurantApprovalRequestAvroModel.getOrderId())
                .sagaId(restaurantApprovalRequestAvroModel.getSagaId())
                .restaurantOrderStatus(RestaurantOrderStatus.valueOf(restaurantApprovalRequestAvroModel.getRestaurantOrderStatus().name()))
                .products(restaurantApprovalRequestAvroModel.getProducts().stream().map(this::productAvroModelToProductDomain).toList())
                .price(restaurantApprovalRequestAvroModel.getPrice())
                .createdAt(restaurantApprovalRequestAvroModel.getCreatedAt())
                .build();
    }

    private Product productAvroModelToProductDomain(com.food.ordering.system.kafka.order.avro.model.Product productAvro) {
        return Product.builder()
                .productId(new ProductId(UUID.fromString(productAvro.getId())))
                .quantity(productAvro.getQuantity())
                //.available(productAvro)
                .build();
    }

    public RestaurantApprovalResponseAvroModel orderApprovalEventToRestaurantApprovalResponseAvroModel(OrderApprovalEvent domainEvent) {
        return RestaurantApprovalResponseAvroModel.newBuilder()
                .setOrderId(domainEvent.getOrderApproval().getOrderId().getValue().toString())
                .setRestaurantId(domainEvent.getRestaurantId().getValue().toString())
                .setOrderApprovalStatus(OrderApprovalStatus.valueOf(domainEvent.getOrderApproval().getOrderApprovalStatus().toString()))
                .setId(domainEvent.getOrderApproval().getId().getValue().toString())
                //.setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setCreatedAt(domainEvent.getCreatedAt().toInstant())
                .setFailureMessages(domainEvent.getFailureMessage())
                .build();
    }
}
