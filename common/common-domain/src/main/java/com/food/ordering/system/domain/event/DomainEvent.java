package com.food.ordering.system.domain.event;

public interface DomainEvent<T> {
    /**
     * Publish message to the kafka broker.
     * */
    void fire();
}
