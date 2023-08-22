package com.food.ordering.system.restaurant.service.domain.outbox.scheduler;

import com.food.ordering.system.outbox.OutboxScheduler;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.restaurant.service.domain.outbox.model.OrderOutboxMessage;
import com.food.ordering.system.restaurant.service.domain.ports.output.message.publisher.RestaurantApprovalResponseMessagePublisher;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor
public class OrderOutboxScheduler implements OutboxScheduler {
    private final OrderOutboxHelper orderOutboxHelper;
    private final RestaurantApprovalResponseMessagePublisher restaurantApprovalResponseMessagePublisher;
    @Override
    @Scheduled(fixedRateString = "${restaurant-service.outbox-scheduler-fixed-rate}",
            initialDelayString = "${restaurant-service.outbox-scheduler-initial-delay}")
    public void processOutboxMessage() {
        Optional<List<OrderOutboxMessage>> outboxMessages = orderOutboxHelper.findByOutboxStatus(OutboxStatus.STARTED);

        if (outboxMessages.isPresent() && outboxMessages.get().size() > 0) {
            List<OrderOutboxMessage> orderOutboxMessages = outboxMessages.get();
            log.info("Received {} OrderOutboxMessage with ids {}, sending to kafka!", orderOutboxMessages.size(),
                    orderOutboxMessages.stream()
                            .map(outboxMessage -> outboxMessage.getId().toString()).collect(Collectors.joining(",")));

            orderOutboxMessages.forEach(orderOutboxMessage -> restaurantApprovalResponseMessagePublisher
                    .publish(orderOutboxMessage, orderOutboxHelper::updateOutboxMessage));

            log.info("{} OrderOutboxMessage sent to message bus!", orderOutboxMessages.size());
        }
    }
}
