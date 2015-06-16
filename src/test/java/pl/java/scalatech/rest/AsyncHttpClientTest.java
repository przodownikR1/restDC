package pl.java.scalatech.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.AsyncClientHttpRequest;
import org.springframework.http.client.AsyncClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.util.concurrent.ListenableFuture;
// compile 'org.apache.httpcomponents:httpclient:4.5'
// compile 'org.apache.httpcomponents:httpasyncclient:4.1'

@Slf4j
public class AsyncHttpClientTest {
    @Test
    public void shouldGoogleResponse() throws IOException, InterruptedException, ExecutionException, URISyntaxException {
        AsyncClientHttpRequestFactory asyncFactory = new HttpComponentsAsyncClientHttpRequestFactory();
        URI uri = new URI("http://facebook.com");
        AsyncClientHttpRequest asynReq = asyncFactory.createAsyncRequest(uri, HttpMethod.GET);
        ListenableFuture<ClientHttpResponse> future = asynReq.executeAsync();
        ClientHttpResponse response = future.get();

        log.info("++++  {} , {} ", response.getStatusCode(), response.getHeaders());
    }

}
