package com.food.ordering.system.order.service.domain.outbox.scheduler.approval;

import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.food.ordering.system.outbox.OutboxScheduler;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.saga.SagaStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class ApprovalOutboxCleanerScheduler implements OutboxScheduler {
    private final ApprovalOutboxHelper approvalOutboxHelper;
    @Override
    @Scheduled(cron = "@midnight")
    public void processOutboxMessage() {
        Optional<List<OrderApprovalOutboxMessage>> outboxMessagesResponse =
                approvalOutboxHelper.getApprovalOutboxMessageByOutboxStatusAndSagaStatus(
                        OutboxStatus.STARTED,
                        SagaStatus.SUCCEEDED,
                        SagaStatus.COMPENSATED,
                        SagaStatus.FAILED
                );

        if (outboxMessagesResponse.isPresent()) {
            List<OrderApprovalOutboxMessage> outboxMessages = outboxMessagesResponse.get();
            log.info("Received {} OrderApprovalOutboxMessage for clean up. The payload: {}",
                    outboxMessages.size(),
                    outboxMessages.stream().map(OrderApprovalOutboxMessage::getPayload).collect(Collectors.joining("\n")));

            approvalOutboxHelper
                    .deleteApprovalOutboxMessageByOutboxStatusAndSagaStatus(
                            OutboxStatus.STARTED,
                            SagaStatus.SUCCEEDED,
                            SagaStatus.COMPENSATED,
                            SagaStatus.FAILED);
            log.info("{} OrderApprovalOutboxMessage deleted", outboxMessages.size());
        }
    }
}
