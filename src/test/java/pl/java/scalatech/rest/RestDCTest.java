package pl.java.scalatech.rest;

import static org.hamcrest.Matchers.is;
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
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import pl.java.scalatech.RestStubDcApplication;
import pl.java.scalatech.entity.BankAccount;
import pl.java.scalatech.repository.BankAccountRepository;
import pl.java.scalatech.web.HealthController;
import pl.java.scalatech.web.RestContentNegotiatingController;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RestStubDcApplication.class)
@WebAppConfiguration
public class RestDCTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldBootstrap() {
    }

    @Test
    public void shouldHealthControllerResponse() throws Exception {
        this.mockMvc.perform(get(HealthController.URL)).andExpect(status().isOk()).andExpect(content().string(Matchers.not(Matchers.isEmptyOrNullString())));
    }

    @Test
    public void shouldRestContextNegotiationJsonWork() throws Exception {
        this.mockMvc.perform(get(RestContentNegotiatingController.URL).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json")).andExpect(jsonPath("$.creditCardNumer").value("120003430023"))
                .andExpect(jsonPath("$.debit").value(900)).andExpect(jsonPath("$.amount", is(1000)));
        String response = this.mockMvc.perform(get(RestContentNegotiatingController.URL)).andReturn().getResponse().getContentAsString();
        Log.info("+++ cn  response {}", response);

    }

    @Test
    public void shouldRestContextNegotiationJsonWithParamsWork() throws Exception {
        Mockito.when(bankAccountRepository.findOne(1l)).thenReturn(
                BankAccount.builder().amount(new BigDecimal("1000")).creditCardNumer("120003430023").debit(new BigDecimal("900")).build());

        this.mockMvc.perform(get(RestContentNegotiatingController.URL).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json")).andExpect(jsonPath("$.creditCardNumer").value("120003430023"))
                .andExpect(jsonPath("$.debit").value(900)).andExpect(jsonPath("$.amount", is(1000)));
        String response = this.mockMvc.perform(get(RestContentNegotiatingController.URL)).andReturn().getResponse().getContentAsString();
        Log.info("+++ cn  response {}", response);

    }

    @Test
    public void shouldRestContextNegotiationXmlWork() throws Exception {
        this.mockMvc.perform(get(RestContentNegotiatingController.URL).accept(MediaType.APPLICATION_XML_VALUE)).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/xml"));
        String response = this.mockMvc.perform(get(RestContentNegotiatingController.URL)).andReturn().getResponse().getContentAsString();
        Log.info("+++ cn  response {}", response);

    }
}
