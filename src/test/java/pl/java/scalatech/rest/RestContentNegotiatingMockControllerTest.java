package pl.java.scalatech.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.crsh.console.jline.internal.Log;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import pl.java.scalatech.config.ApplicationConfig;
import pl.java.scalatech.entity.BankAccount;
import pl.java.scalatech.repository.BankAccountRepository;
import pl.java.scalatech.web.RestContentNegotiatingController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
@WebAppConfiguration
@IntegrationTest
public class RestContentNegotiatingMockControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private RestContentNegotiatingController restContentNegotiatingController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(restContentNegotiatingController).build();
    }

    @Test
    public void shouldRestContextNegotiationJsonWithParamsWork() throws Exception {
        Mockito.when(bankAccountRepository.findOne(1l)).thenReturn(
                BankAccount.builder().amount(new BigDecimal("1000")).creditCardNumer("120003430023").debit(new BigDecimal("900")).build());

        this.mockMvc.perform(get(RestContentNegotiatingController.URL + "/{id}", 1).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json")).andExpect(jsonPath("$.creditCardNumer").value("120003430023"))
                .andExpect(jsonPath("$.debit").value(900d)).andExpect(jsonPath("$.amount", Matchers.is(1000.0)));

        this.mockMvc.perform(get(RestContentNegotiatingController.URL + "/{id}", 1).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().string(Matchers.not(Matchers.isEmptyOrNullString())));

        String response = this.mockMvc.perform(get(RestContentNegotiatingController.URL + "/{id}", 1)).andReturn().getResponse().getContentAsString();
        Log.info("+++ cn  response {}", response);

    }

}
