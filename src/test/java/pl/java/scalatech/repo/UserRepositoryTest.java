package pl.java.scalatech.repo;

import static com.google.common.collect.Lists.newArrayList;
import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.java.scalatech.config.TransactionConfig;
import pl.java.scalatech.entity.BankAccount;
import pl.java.scalatech.entity.User;
import pl.java.scalatech.repository.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TransactionConfig.class)
@Slf4j
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldUserRepositoryWork() {
        // given
        BankAccount ba = BankAccount.builder().amount(new BigDecimal("2000")).credit(new BigDecimal(140)).creditCardNumer("1111000000112").build();
        User user = User.builder().city("Warsaw").email("przodownikR1@gmail.com").nip("7962443170").login("przodownik").phone("5161674900")
                .address("krypska 20").accounts(newArrayList(ba)).build();
        // when
        User loaded = userRepository.save(user);
        // then
        assertThat(loaded.getId()).isNotNull();
        assertThat(loaded.getAccounts()).hasSize(1);
        log.info("{}", loaded);
    }
}
