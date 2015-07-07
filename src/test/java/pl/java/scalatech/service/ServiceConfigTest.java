package pl.java.scalatech.service;

import org.fest.assertions.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.java.scalatech.config.TransactionConfig;
import pl.java.scalatech.service.userInformation.UserAccountService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TransactionConfig.class)
@IntegrationTest(value = "it")
public class ServiceConfigTest {
    @Autowired
    private UserAccountService userAccountService;

    @Test
    public void shouldBootstrapConfig() {
        Assertions.assertThat(userAccountService).isNotNull();
    }

}
