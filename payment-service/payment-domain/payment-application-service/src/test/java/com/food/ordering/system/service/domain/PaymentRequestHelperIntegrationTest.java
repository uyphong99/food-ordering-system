package com.food.ordering.system.service.domain;

import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.OrderId;
import com.food.ordering.system.domain.valueobject.PaymentOrderStatus;
import com.food.ordering.system.payment.service.domain.PaymentDomainService;
import com.food.ordering.system.payment.service.domain.PaymentRequestHelper;
import com.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.entity.CreditEntry;
import com.food.ordering.system.payment.service.domain.entity.CreditHistory;
import com.food.ordering.system.payment.service.domain.event.PaymentEvent;
import com.food.ordering.system.payment.service.domain.mapper.PaymentDataMapper;
import com.food.ordering.system.payment.service.domain.ports.output.repository.CreditEntryRepository;
import com.food.ordering.system.payment.service.domain.ports.output.repository.CreditHistoryRepository;
import com.food.ordering.system.payment.service.domain.valueobject.CreditEntryId;
import com.food.ordering.system.payment.service.domain.valueobject.CreditHistoryId;
import com.food.ordering.system.payment.service.domain.valueobject.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = PaymentTestConfiguration.class)
public class PaymentRequestHelperIntegrationTest {
    private PaymentRequestHelper paymentRequestHelper;
    private PaymentDomainService paymentDomainService;
    private PaymentDataMapper paymentDataMapper;
    private PaymentRequest paymentRequestSuccess;
    private PaymentRequest paymentRequestInsufficientCredit;
    private PaymentRequest paymentRequestInvalidPrice;
    private PaymentRequest paymentRequestInvalidCreditHistory;
    private CreditEntry creditEntry;
    private CreditHistory creditHistory_1;
    private CreditHistory creditHistory_2;
    private CreditHistory creditHistory_3;
    private CreditHistoryRepository creditHistoryRepository;
    private CreditEntryRepository creditEntryRepository;
    private final UUID CUSTOMER_ID = UUID.randomUUID();
    private final UUID ORDER_ID = UUID.randomUUID();
    private final UUID CREDITENTRY_ID = UUID.fromString("781a3e15-8a9e-4e37-b8f8-80537b6dc8b7");
    private final UUID CREDITHISTORY_ID = UUID.fromString("781a3e15-8a9e-4e37-b8f8-80537b6dc8b8");
    private final BigDecimal PRICE = new BigDecimal("100.00");

    public PaymentRequestHelperIntegrationTest(@Autowired PaymentRequestHelper paymentRequestHelper,
                                               @Autowired PaymentDomainService paymentDomainService,
                                               @Autowired PaymentDataMapper paymentDataMapper,
                                               @Autowired CreditHistoryRepository creditHistoryRepository,
                                               @Autowired CreditEntryRepository creditEntryRepository) {
        this.paymentRequestHelper = paymentRequestHelper;
        this.paymentDomainService = paymentDomainService;
        this.paymentDataMapper = paymentDataMapper;
        this.creditHistoryRepository = creditHistoryRepository;
        this.creditEntryRepository = creditEntryRepository;
    }

    @BeforeEach
    void init() {
        CustomerId customerId = new CustomerId(CUSTOMER_ID);
        OrderId orderId = new OrderId(ORDER_ID);

        paymentRequestInsufficientCredit = PaymentRequest.builder()
                .customerId(CUSTOMER_ID.toString())
                .orderId(ORDER_ID.toString())
                .price(PRICE)
                .paymentOrderStatus(PaymentOrderStatus.PENDING)
                .build();

        paymentRequestInvalidPrice = PaymentRequest.builder()
                .customerId(CUSTOMER_ID.toString())
                .orderId(ORDER_ID.toString())
                .price((new BigDecimal("50.00")))
                .paymentOrderStatus(PaymentOrderStatus.PENDING)
                .build();

        paymentRequestInvalidCreditHistory = PaymentRequest.builder()
                .customerId(CUSTOMER_ID.toString())
                .orderId(ORDER_ID.toString())
                .price(PRICE)
                .paymentOrderStatus(PaymentOrderStatus.PENDING)
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
                .customerId(customerId)
                .build();

        creditEntry = CreditEntry.builder()
                .creditEntryId(new CreditEntryId(UUID.randomUUID()))
                .totalCreditAmount(new Money(new BigDecimal("200")))
                .customerId(customerId)
                .build();

        when(creditEntryRepository.save(any(CreditEntry.class))).thenReturn(creditEntry);
        when(creditHistoryRepository.save(creditHistory_2)).thenReturn(creditHistory_2);
        when(creditHistoryRepository.save(creditHistory_1)).thenReturn(creditHistory_1);
        when(creditHistoryRepository.save(creditHistory_3)).thenReturn(creditHistory_3);

        when(creditEntryRepository.findByCustomerId(customerId)).thenReturn(Optional.ofNullable(creditEntry));
    }

    @Test
    void testPersistPayment_whenPriceIsNull() {
        /* List<CreditHistory> creditHistoriesList = Arrays.asList(creditHistory_1, creditHistory_2);
        Optional<List<CreditHistory>> creditHistories = Optional.of(creditHistoriesList);

        when(creditHistoryRepository.findByCustomerId(new CustomerId(CUSTOMER_ID))).thenReturn(creditHistories);

        PaymentEvent paymentEvent = paymentRequestHelper.persistPayment(paymentRequestInvalidPrice);
        assertNotNull(paymentEvent);

         */
    }

    @Test
    void testPersistPayment_whenSuccess() {

    }

    @Test
    void testPersistPayment_whenCustomerHaveInsufficientCreditEntry() {

    }

    @Test
    void testPersistPayment_whenHavingInvalidCreditHistory() {

    }


}
