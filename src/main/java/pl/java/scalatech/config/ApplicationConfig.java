package pl.java.scalatech.config;

import java.math.BigDecimal;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import pl.java.scalatech.entity.BankAccount;
import pl.java.scalatech.repository.BankAccountRepository;

@Configuration
@ComponentScan(value = "pl.java.scalatech")
@EnableAutoConfiguration
// used to search for @Entity items and entityManagerFactory ect.
@PropertySource("classpath:application.properties")
@Import(TransactionConfig.class)
public class ApplicationConfig {
    @Autowired
    private BankAccountRepository bankAccountRepository;

    @PostConstruct
    public void initForTest() {
        bankAccountRepository.save(BankAccount.builder().amount(new BigDecimal("1000")).creditCardNumer("120003430023").debit(new BigDecimal("900")).build());
        bankAccountRepository.save(BankAccount.builder().amount(new BigDecimal("300")).creditCardNumer("75003432343").credit(new BigDecimal("200")).build());
    }

}
