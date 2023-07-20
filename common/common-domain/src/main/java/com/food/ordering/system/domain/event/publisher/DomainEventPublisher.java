package com.food.ordering.system.domain.event.publisher;

import com.food.ordering.system.domain.event.DomainEvent;
/**
 * Base interface, will be extended by publisher ports
 * */
public interface DomainEventPublisher<T extends DomainEvent> {
    void publish(T domainEvent);
}
