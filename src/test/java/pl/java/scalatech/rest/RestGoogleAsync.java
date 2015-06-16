package pl.java.scalatech.rest;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRequestCallback;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.ResponseExtractor;

import com.google.common.collect.Maps;

@Slf4j
public class RestGoogleAsync {
    @Test
    public void shouldGoogleResponse() {
        AsyncRestTemplate asycTemp = new AsyncRestTemplate();
        String url = "http://google.com";
        HttpMethod method = HttpMethod.GET;
        Class<String> responseType = String.class;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        HttpEntity<String> requestEntity = new HttpEntity<>("params", headers);
        ListenableFuture<ResponseEntity<String>> future = asycTemp.exchange(url, method, requestEntity, responseType);
        try {
            // waits for the result
            ResponseEntity<String> entity = future.get();
            // prints body source code for the given URL
            log.info("+++  shouldGoogleResponse -> body :  {}" + entity.getBody());
        } catch (InterruptedException | ExecutionException e) {
            log.error("{}", e);
        }
    }

    @Test
    public void shouldGoogleResponseListenerUse() {
        AsyncRestTemplate asycTemp = new AsyncRestTemplate();
        String url = "http://google.com";
        HttpMethod method = HttpMethod.GET;
        // create request entity using HttpHeaders
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        AsyncRequestCallback requestCallback = arg0 -> log.error(" AsyncRequestCallback  : -> {}", arg0.getURI());

        ResponseExtractor<String> responseExtractor = arg0 -> arg0.getStatusText();

        Map<String, String> urlVariable = Maps.newHashMap();
        urlVariable.put("q", "Concretepage");
        ListenableFuture<String> future = asycTemp.execute(url, method, requestCallback, responseExtractor, urlVariable);
        try {
            // waits for the result
            String result = future.get();
            log.info("+++ shouldGoogleResponseListenerUse -> body {}", result);
        } catch (InterruptedException | ExecutionException e) {
            log.error("{}", e);

        }
    }
}
