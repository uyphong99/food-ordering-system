package com.food.ordering.system.payment.service.domain;

import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.PaymentStatus;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.entity.CreditEntry;
import com.food.ordering.system.payment.service.domain.entity.CreditHistory;
import com.food.ordering.system.payment.service.domain.entity.Payment;
import com.food.ordering.system.payment.service.domain.event.PaymentEvent;
import com.food.ordering.system.payment.service.domain.exception.PaymentApplicationServiceException;
import com.food.ordering.system.payment.service.domain.exception.PaymentNotFoundException;
import com.food.ordering.system.payment.service.domain.mapper.PaymentDataMapper;
import com.food.ordering.system.payment.service.domain.outbox.model.OrderOutboxMessage;
import com.food.ordering.system.payment.service.domain.outbox.scheduler.OrderOutboxHelper;
import com.food.ordering.system.payment.service.domain.ports.output.message.publisher.PaymentResponseMessagePublisher;
import com.food.ordering.system.payment.service.domain.ports.output.repository.CreditEntryRepository;
import com.food.ordering.system.payment.service.domain.ports.output.repository.CreditHistoryRepository;
import com.food.ordering.system.payment.service.domain.ports.output.repository.PaymentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
@Slf4j
public class PaymentRequestHelper {
    private final PaymentDomainService paymentDomainService;
    private final PaymentDataMapper paymentDataMapper;
    private final PaymentRepository paymentRepository;
    private final CreditEntryRepository creditEntryRepository;
    private final CreditHistoryRepository creditHistoryRepository;
    private final OrderOutboxHelper orderOutboxHelper;
    private final PaymentResponseMessagePublisher paymentResponseMessagePublisher;


    @Transactional
    public void persistPayment(PaymentRequest paymentRequest) {

        if (publishIfOutboxMessageProcessedForPayment(paymentRequest, PaymentStatus.COMPLETED)) {
            log.info("An outbox message with saga id: {} is already saved to database!", paymentRequest.getSagaId());
            return;
        }

        Payment payment = paymentDataMapper.paymentRequestToPayment(paymentRequest);

        log.info("Received payment complete event for order id: {}", paymentRequest.getOrderId());
        boolean existPayment = false;

        String sagaId = paymentRequest.getSagaId();
        PaymentEvent paymentEvent = paymentToPaymentEvent(payment, existPayment);

        orderOutboxHelper.saveOrderOutboxMessage(paymentDataMapper.eventToPayload(paymentEvent),
                paymentEvent.getPayment().getPaymentStatus(), OutboxStatus.STARTED, UUID.fromString(sagaId));
    }

    @Transactional
    public void persistCancelPayment(PaymentRequest paymentRequest) {

        if (publishIfOutboxMessageProcessedForPayment(paymentRequest, PaymentStatus.CANCELLED)) {
            log.info("An outbox message with saga id: {} is already saved to database!", paymentRequest.getSagaId());
            return;
        }

        log.info("Received payment rollback event for order id: {}", paymentRequest.getOrderId());
        Optional<Payment> paymentResponse = paymentRepository.findByOrderId(UUID.fromString(paymentRequest.getOrderId()));
        if (paymentResponse.isEmpty()) {
            log.error("Payment with order id: {} could not be found", paymentRequest.getOrderId());
            throw new PaymentNotFoundException("Payment with order id: " +
                    paymentRequest.getOrderId() + " could not be found!");
        }

        boolean existPayment = true;

        Payment payment = paymentResponse.get();
        String sagaId = paymentRequest.getSagaId();

        PaymentEvent paymentEvent = paymentToPaymentEvent(payment, existPayment);

        orderOutboxHelper.saveOrderOutboxMessage(paymentDataMapper.eventToPayload(paymentEvent),
                paymentEvent.getPayment().getPaymentStatus(), OutboxStatus.STARTED, UUID.fromString(sagaId));
    }

    /**
     * map payment to payment event and persist entry, entry history and payment to DB
     * */
    private PaymentEvent paymentToPaymentEvent(Payment payment, boolean existPayment) {
        CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
        List<CreditHistory> creditHistories = getCreditHistories(payment.getCustomerId());
        List<String> failureMessages = new ArrayList<>();

        PaymentEvent paymentEvent;

        if (existPayment) {
            paymentEvent = paymentDomainService.validateAndCancelPayment(payment, creditEntry, creditHistories, failureMessages);
        } else {
            paymentEvent = paymentDomainService.validateAndInitiatePayment(payment, creditEntry, creditHistories, failureMessages);
        }

        persistDbObject(payment, creditEntry, creditHistories, failureMessages);

        return paymentEvent;
    }

    private void persistDbObject(Payment payment, CreditEntry creditEntry, List<CreditHistory> creditHistories, List<String> failureMessages) {
        paymentRepository.save(payment);

        if (failureMessages.isEmpty()) {
            creditEntryRepository.save(creditEntry);
            creditHistoryRepository.save(creditHistories.get(creditHistories.size() - 1));
        }
    }

    private List<CreditHistory> getCreditHistories(CustomerId customerId) {
        Optional<List<CreditHistory>> creditHistories = creditHistoryRepository.findByCustomerId(customerId);

        if (creditHistories.isEmpty()) {
            log.error("Could not find credit history for customer: {}", customerId.getValue());
            throw new PaymentApplicationServiceException("Could not find credit history for customer: " +
                    customerId.getValue());
        }
        return creditHistories.get();
    }

    private CreditEntry getCreditEntry(CustomerId customerId) {
        Optional<CreditEntry> creditEntry = creditEntryRepository.findByCustomerId(customerId);

        if (creditEntry.isEmpty()) {
            log.error("Could not find credit entry for customer id: {}", customerId.getValue());
            throw new PaymentApplicationServiceException("Could not find credit entry for customer: {}" +
                    customerId.getValue());
        }

        return creditEntry.get();
    }

    private boolean publishIfOutboxMessageProcessedForPayment(PaymentRequest paymentRequest, PaymentStatus paymentStatus) {
        Optional<OrderOutboxMessage> orderOutboxMessage = orderOutboxHelper
                .getCompletedOrderOutboxMessageBySagaIdAndPaymentStatus(UUID.fromString(paymentRequest.getSagaId()), paymentStatus);

        if (orderOutboxMessage.isPresent()) {
            paymentResponseMessagePublisher.publish(orderOutboxMessage.get(), orderOutboxHelper::updateOutboxMessage);
            return true;
        }
        return false;
    }
}
