package pl.java.scalatech.rest;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import pl.java.scalatech.config.TransactionConfig;
import pl.java.scalatech.config.WebConfig;
import pl.java.scalatech.repository.BankAccountRepository;
import pl.java.scalatech.repository.UserRepository;
import pl.java.scalatech.web.UserInfoController;

@ContextConfiguration(classes = { WebConfig.class, TransactionConfig.class })
@WebAppConfiguration
@IntegrationTest
public class UserInfoControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private UserRepository userRepository;

    @Autowired
    private UserInfoController userInfoController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userInfoController).build();
    }

}
