package pl.java.scalatech.web;

import java.math.BigDecimal;

import javax.annotation.PostConstruct;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.java.scalatech.entity.BankAccount;
import pl.java.scalatech.repository.BankAccountRepository;

@RestController
@Slf4j
@RequestMapping(value = RestContentNegotiatingController.URL)
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@Profile("webTest")
public class RestContentNegotiatingController {

    public final static String URL = "/cn";

    private final @NonNull BankAccountRepository bankAccountRepository;

    @PostConstruct
    public void init() {
        log.info("+++  RestContentNegotiatingController init");
    }

    @RequestMapping(produces = "application/json")
    public BankAccount ba() {
        return BankAccount.builder().amount(new BigDecimal("1000")).creditCardNumer("120003430023").debit(new BigDecimal("900")).build();
    }

    @RequestMapping(value = "/{id}", produces = "application/json")
    public BankAccount baById(@PathVariable("id") Long id) {
        log.info("+++  :: id = {}", id);
        return bankAccountRepository.findOne(id);
    }

}
