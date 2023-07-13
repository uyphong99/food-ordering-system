package com.food.ordering.system.restaurant.service.domain.ports.output.repository;

import com.food.ordering.system.restaurant.service.domain.entity.OrderApproval;
import com.food.ordering.system.restaurant.service.domain.event.OrderApprovalEvent;

public interface OrderApprovalRepository {
    OrderApproval save(OrderApproval orderApproval);
}
