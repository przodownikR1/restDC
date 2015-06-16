package pl.java.scalatech.rest;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;

import pl.java.scalatech.entity.User;

import com.google.common.collect.Lists;

@Slf4j
public class RestAsyncTest {
    String baseUrl = "http://localhost:8777";

    @Test
    public void shouldGetUserAsync() {
        AsyncRestTemplate restTemplate = new AsyncRestTemplate();

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));

        // requestHeaders.add("Cookie", cookie);

        log.info(" requestHeaders :  {} ", requestHeaders);

        HttpEntity<String> entity = new HttpEntity<>("parameters", requestHeaders);

        Future<ResponseEntity<String>> futureEntity = restTemplate.exchange(baseUrl + "api/async/users/login/{login}", HttpMethod.GET, entity, String.class,
                "przodownik");

        ResponseEntity<String> result = null;
        try {
            result = futureEntity.get();
            log.info("Response received");
            System.out.println(result.getBody());

        } catch (InterruptedException | ExecutionException e) {
            log.error("{}", e);

        }

    }

    @Test
    public void shouldGetUserAsyncWithCallback() {
        AsyncRestTemplate restTemplate = new AsyncRestTemplate();

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));

        // requestHeaders.add("Cookie", cookie);

        log.info(" requestHeaders :  {} ", requestHeaders);

        HttpEntity<String> entity = new HttpEntity<>("parameters", requestHeaders);

        ListenableFuture<ResponseEntity<User>> futureEntity = restTemplate.exchange(baseUrl + "api/async/users/login/{login}", HttpMethod.GET, entity,
                User.class, "przodownik");

        futureEntity.addCallback(new ListenableFutureCallback<ResponseEntity<User>>() {
            @Override
            public void onSuccess(ResponseEntity<User> result) {
                log.info("Response received (async callable)");
                log.info("+++ result :  {}", result.getBody());
                // Need assertions
            }

            @Override
            public void onFailure(Throwable t) {
                log.error("{}", t);
            }
        });

        log.info("Doing other async callable stuff ...");
        try {
            // waits for the service to send the response
            Thread.sleep(1);
        } catch (InterruptedException e) {
            log.error("{}", e);
        }

    }
}
