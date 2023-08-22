package com.food.ordering.system.restaurant.service.dataaccess.restaurant.outbox.adapter;

import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.restaurant.service.dataaccess.restaurant.outbox.entity.OrderOutboxEntity;
import com.food.ordering.system.restaurant.service.dataaccess.restaurant.outbox.exception.OrderOutboxNotFoundException;
import com.food.ordering.system.restaurant.service.dataaccess.restaurant.outbox.mapper.OrderOutboxDataAccessMapper;
import com.food.ordering.system.restaurant.service.dataaccess.restaurant.outbox.repository.OrderOutboxJpaRepository;
import com.food.ordering.system.restaurant.service.domain.outbox.model.OrderOutboxMessage;
import com.food.ordering.system.restaurant.service.domain.ports.output.repository.OrderOutboxRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class OrderOutboxRepositoryImpl implements OrderOutboxRepository {
    private final OrderOutboxJpaRepository orderOutboxJpaRepository;
    private final OrderOutboxDataAccessMapper orderOutboxDataAccessMapper;
    @Override
    public OrderOutboxMessage save(OrderOutboxMessage orderOutboxMessage) {
        return orderOutboxDataAccessMapper.entityToDomain(
                orderOutboxJpaRepository.save(orderOutboxDataAccessMapper.domainToEntity(orderOutboxMessage)));
    }

    @Override
    public Optional<List<OrderOutboxMessage>> findBySagaTypeAndOutboxStatus(String sagaType, OutboxStatus outboxStatus) {
        Optional<List<OrderOutboxEntity>> orderOutboxEntities =
                Optional.ofNullable(orderOutboxJpaRepository.findByTypeAndOutboxStatus(sagaType, outboxStatus)
                .orElseThrow(() -> new OrderOutboxNotFoundException("No matching entity found")));

        return orderOutboxEntities.map(entities -> entities.stream()
                .map(orderOutboxDataAccessMapper::entityToDomain)
                .collect(Collectors.toList()));
    }

    @Override
    public void deleteBySagaTypeAndOutboxStatus(String type, OutboxStatus outboxStatus) {
        orderOutboxJpaRepository.deleteByTypeAndOutboxStatus(type, outboxStatus);
    }
}
