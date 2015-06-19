package pl.java.scalatech.rest;

import lombok.extern.slf4j.Slf4j;

import org.fest.assertions.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import pl.java.scalatech.RestStubDcApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RestStubDcApplication.class)
@WebAppConfiguration
@IntegrationTest({ "debug=true", "server.port=9000" })
@Slf4j
public class SimpleRestClientTest {

    private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void shouldPongReturn() {
        String url = "http://localhost:9000/ping";

        String result = restTemplate.getForObject(url, String.class);
        Assertions.assertThat(result).isEqualTo("pong");

    }
}
