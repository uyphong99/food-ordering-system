package com.food.ordering.system.service.domain.service;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.OrderId;
import com.food.ordering.system.domain.valueobject.PaymentStatus;
import com.food.ordering.system.payment.service.domain.PaymentDomainService;
import com.food.ordering.system.payment.service.domain.PaymentDomainServiceImpl;
import com.food.ordering.system.payment.service.domain.entity.CreditEntry;
import com.food.ordering.system.payment.service.domain.entity.CreditHistory;
import com.food.ordering.system.payment.service.domain.entity.Payment;
import com.food.ordering.system.payment.service.domain.event.PaymentCompletedEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentFailedEvent;
import com.food.ordering.system.payment.service.domain.valueobject.CreditEntryId;
import com.food.ordering.system.payment.service.domain.valueobject.CreditHistoryId;
import com.food.ordering.system.payment.service.domain.valueobject.PaymentId;
import com.food.ordering.system.payment.service.domain.valueobject.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.food.ordering.system.domain.DomainConstants.UTC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PaymentDomainServiceTest {
    @Mock
    private DomainEventPublisher<PaymentCompletedEvent> paymentCompletedEventDomainEventPublisher;

    @Mock
    private DomainEventPublisher<PaymentFailedEvent> paymentFailedEventPublisher;

    private PaymentDomainService paymentDomainService;
    private Payment paymentWithNullPrice;
    private Payment paymentExpectedSuccess;
    private Payment paymentWithHighPrice;
    private CreditEntry creditEntry;
    private CreditHistory creditHistory_1;
    private CreditHistory creditHistory_2;
    private CreditHistory creditHistory_3;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        paymentDomainService = new PaymentDomainServiceImpl();
        CustomerId customerId = new CustomerId(UUID.randomUUID());

        paymentExpectedSuccess = Payment.builder()
                .paymentId(new PaymentId(UUID.randomUUID()))
                .customerId(customerId)
                .price(new Money(new BigDecimal("100.00")))
                .orderId(new OrderId(UUID.randomUUID()))
                .createdAt(ZonedDateTime.now(ZoneId.of(UTC)))
                .build();

        paymentWithNullPrice = Payment.builder()
                .paymentId(new PaymentId(UUID.randomUUID()))
                .customerId(customerId)
                .price(null)
                .orderId(new OrderId(UUID.randomUUID()))
                .createdAt(ZonedDateTime.now(ZoneId.of(UTC)))
                .build();

        paymentWithHighPrice = Payment.builder()
                .paymentId(new PaymentId(UUID.randomUUID()))
                .customerId(customerId)
                .price(new Money(new BigDecimal("300.00")))
                .orderId(new OrderId(UUID.randomUUID()))
                .createdAt(ZonedDateTime.now(ZoneId.of(UTC)))
                .build();

        creditHistory_1 = CreditHistory.builder()
                .creditHistoryId(new CreditHistoryId(UUID.randomUUID()))
                .transactionType(TransactionType.DEBIT)
                .money(new Money(new BigDecimal("50.00")))
                .customerId(customerId)
                .build();

        creditHistory_2 = CreditHistory.builder()
                .creditHistoryId(new CreditHistoryId(UUID.randomUUID()))
                .transactionType(TransactionType.CREDIT)
                .money(new Money(new BigDecimal("250.00")))
                .customerId(customerId)
                .build();

        creditHistory_3 = CreditHistory.builder()
                .creditHistoryId(new CreditHistoryId(UUID.randomUUID()))
                .transactionType(TransactionType.CREDIT)
                .money(new Money(new BigDecimal("280.00")))
                //.customerId(customerId)
                .build();

        creditEntry = CreditEntry.builder()
                .creditEntryId(new CreditEntryId(UUID.randomUUID()))
                .totalCreditAmount(new Money(new BigDecimal("200")))
                .customerId(customerId)
                .build();
    }

    @Test
    void testValidateAndInitiatePayment_successCase() {
        List<CreditHistory> creditHistories = new ArrayList<>();
        creditHistories.addAll(List.of(creditHistory_1, creditHistory_2));
        List<String> failureMessages = new ArrayList<>();

        PaymentEvent paymentEvent = paymentDomainService.validateAndInitiatePayment(
                paymentExpectedSuccess, creditEntry,
                creditHistories, failureMessages,
                paymentCompletedEventDomainEventPublisher,
                paymentFailedEventPublisher);

        assertEquals(PaymentStatus.COMPLETED, paymentExpectedSuccess.getPaymentStatus());
        assertEquals(PaymentCompletedEvent.class, paymentEvent.getClass());
        assertNotNull(paymentEvent);
        assertEquals(creditEntry.getTotalCreditAmount(), new Money(new BigDecimal("100.00")));
    }

    @Test
    void testValidateAndInitiatePayment_whenPaymentHaveNullPrice() {
        // Arrange
        List<CreditHistory> creditHistories = new ArrayList<>();
        creditHistories.addAll(List.of(creditHistory_1, creditHistory_2));
        List<String> failureMessages = new ArrayList<>();

        // Act
        PaymentEvent paymentEvent = paymentDomainService.validateAndInitiatePayment(
                paymentWithNullPrice, creditEntry,
                creditHistories, failureMessages,
                paymentCompletedEventDomainEventPublisher,
                paymentFailedEventPublisher
        );

        // Assert
        assertEquals(PaymentStatus.FAILED, paymentWithNullPrice.getPaymentStatus());
        assertEquals(PaymentFailedEvent.class, paymentEvent.getClass());
        assertNotNull(paymentEvent);
    }

    @Test
    void testValidateAndInitiatePayment_whenCreditHistoryInvalid() {
        List<CreditHistory> invalidCreditHistories = new ArrayList<>();
        invalidCreditHistories.addAll(List.of(creditHistory_1, creditHistory_3));
        List<String> failureMessages = new ArrayList<>();

        PaymentEvent paymentEvent = paymentDomainService.validateAndInitiatePayment(
                paymentExpectedSuccess, creditEntry,
                invalidCreditHistories, failureMessages,
                paymentCompletedEventDomainEventPublisher,
                paymentFailedEventPublisher
        );

        assertEquals(PaymentStatus.FAILED, paymentExpectedSuccess.getPaymentStatus());
        assertEquals(PaymentFailedEvent.class, paymentEvent.getClass());
        assertNotNull(paymentEvent);
    }

    @Test
    void testValidateAndInitiatePayment_whenPriceExceedCreditEntry() {
        List<CreditHistory> creditHistories = new ArrayList<>();
        creditHistories.addAll(List.of(creditHistory_1, creditHistory_2));
        List<String> failureMessages = new ArrayList<>();

        // Act
        PaymentEvent paymentEvent = paymentDomainService.validateAndInitiatePayment(
                paymentWithHighPrice, creditEntry,
                creditHistories, failureMessages,
                paymentCompletedEventDomainEventPublisher,
                paymentFailedEventPublisher
        );

        // Assert
        assertEquals(PaymentStatus.FAILED, paymentWithHighPrice.getPaymentStatus());
        assertEquals(PaymentFailedEvent.class, paymentEvent.getClass());
        assertNotNull(paymentEvent);
    }
}
