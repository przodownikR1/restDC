package pl.java.scalatech.service;

import java.util.Random;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import pl.java.scalatech.entity.BankAccount;

// @Component
@Slf4j
public class PeriodicInvokeRest {

    @Autowired
    private RestTemplate restTemplate;

    Random r = new Random();

    @Value("${server.port}")
    private String port;

    @Scheduled(fixedDelay = 5500)
    public void periodicInvokeService() {
        String url = "http://localhost:" + port + "/bankAccounts/{id}";
        long id = r.nextInt(9);
        if (id == 0)
            id++;
        log.info("id {}", id);

        ResponseEntity<BankAccount> ba = restTemplate.getForEntity(url, BankAccount.class, id);
        log.info(" bankAccount {id} :  {} ", ba.getBody());
    }
}
