package pl.java.scalatech.web;

import java.math.BigDecimal;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pl.java.scalatech.entity.BankAccount;
import pl.java.scalatech.repository.BankAccountRepository;

@RestController
@RequestMapping(value = RestContentNegotiatingController.URL)
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class RestContentNegotiatingController {

    public final static String URL = "/cn";

    private final @NonNull BankAccountRepository bankAccountRepository;

    @RequestMapping
    @ResponseBody
    public BankAccount ba() {
        return BankAccount.builder().amount(new BigDecimal("1000")).creditCardNumer("120003430023").debit(new BigDecimal("900")).build();
    }

    @RequestMapping("/{id}")
    @ResponseBody
    public BankAccount baById(@PathVariable("id") Long id) {
        return bankAccountRepository.findOne(id);
    }

}
