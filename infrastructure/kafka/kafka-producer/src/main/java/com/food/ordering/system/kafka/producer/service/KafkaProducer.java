package com.food.ordering.system.kafka.producer.service;

import com.food.ordering.system.outbox.OutboxStatus;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.io.Serializable;
import java.util.function.BiConsumer;

public interface KafkaProducer<K extends Serializable, V extends SpecificRecordBase> {
    /**
     * @param key: orderId of the order.
     * @param message: use avro model as the message.
     * */
    <O> void send(String topicName, K key, V message, BiConsumer<O, OutboxStatus> callback, O outboxMessage);

    void sendWithoutCallback(String topicName, K key, V message);

}
