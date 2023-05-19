package com.food.ordering.system.order.service.dataaccess.order.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OrderItemEntityId implements Serializable {
    private Long id;
    private OrderEntity order;


}
