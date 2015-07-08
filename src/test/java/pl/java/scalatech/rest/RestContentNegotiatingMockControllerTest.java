package pl.java.scalatech.rest;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

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
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import pl.java.scalatech.config.TransactionConfig;
import pl.java.scalatech.config.WebConfig;
import pl.java.scalatech.entity.BankAccount;
import pl.java.scalatech.repository.BankAccountRepository;
import pl.java.scalatech.web.RestContentNegotiatingController;

import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebConfig.class, TransactionConfig.class })
@WebAppConfiguration
@IntegrationTest({ "debug=true", "server.port=9000" })
@Slf4j
public class RestContentNegotiatingMockControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private RestTemplate restTemplate = new RestTemplate();

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private RestContentNegotiatingController restContentNegotiatingController;

    @Before
    public void setup() {
        List<HttpMessageConverter<?>> converters = Lists.newArrayList();
        converters.add(new StringHttpMessageConverter());
        converters.add(new Jaxb2RootElementHttpMessageConverter());
        converters.add(new MappingJackson2HttpMessageConverter());
        restTemplate.setMessageConverters(converters);
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
        log.info("+++ cn  response {}", response);

        verify(bankAccountRepository).findOne(1l);
        verifyNoMoreInteractions(bankAccountRepository);

    }

    @Test
    public void shouldRestContextNegotiationJsonWithParamsAndRestTemplateWork() throws Exception {
        Mockito.when(bankAccountRepository.findOne(1l)).thenReturn(
                BankAccount.builder().amount(new BigDecimal("1000")).creditCardNumer("120003430023").debit(new BigDecimal("900")).build());

        String uri = RestContentNegotiatingController.URL + "/{id}";
        ResponseEntity<BankAccount> ba = restTemplate.getForEntity(uri, BankAccount.class, 1l);
        log.info("+++  bank account body :  {}", ba.getBody());

    }
}
