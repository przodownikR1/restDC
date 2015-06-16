package pl.java.scalatech.web;

import java.math.BigDecimal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pl.java.scalatech.entity.BankAccount;

@RestController
@RequestMapping(value = RestContentNegotiatingController.URL)
public class RestContentNegotiatingController {

    public final static String URL = "/cn";

    @RequestMapping
    @ResponseBody
    public BankAccount ba() {
        return BankAccount.builder().amount(new BigDecimal("1000")).creditCardNumer("120003430023").debit(new BigDecimal("900")).build();
    }

}
