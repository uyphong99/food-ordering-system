package com.food.ordering.system.service.domain;

import com.food.ordering.system.payment.service.domain.PaymentDomainService;
import com.food.ordering.system.payment.service.domain.ports.output.repository.CreditEntryRepository;
import com.food.ordering.system.payment.service.domain.ports.output.repository.CreditHistoryRepository;
import com.food.ordering.system.payment.service.domain.ports.output.repository.PaymentRepository;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.food.ordering.system")
public class PaymentTestConfiguration {

    @Bean
    public PaymentRepository paymentRepository() {
        return Mockito.mock(PaymentRepository.class);
    }

    @Bean
    public CreditEntryRepository creditEntryRepository() {
        return Mockito.mock(CreditEntryRepository.class);
    }

    @Bean
    public CreditHistoryRepository creditHistoryRepository() {
        return Mockito.mock(CreditHistoryRepository.class);
    }

    @Bean
    public PaymentDomainService paymentDomainService() {
        return Mockito.mock(PaymentDomainService.class);
    }
}
