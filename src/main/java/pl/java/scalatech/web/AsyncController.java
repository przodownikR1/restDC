package pl.java.scalatech.web;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestOperations;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

@Slf4j
@RestController
@Profile("webTest")
public class AsyncController {

    private final AsyncRestOperations restOperations = new AsyncRestTemplate();

    static class GitContent extends HashMap<String, String> {
    }

    @RequestMapping("/git")
    public DeferredResult<GitContent> github() throws ExecutionException, InterruptedException {
        final DeferredResult<GitContent> deferredResult = new DeferredResult<>();
        final ListenableFuture<ResponseEntity<GitContent>> future = restOperations.getForEntity("https://api.github.com", GitContent.class);
        future.addCallback(successHandler(deferredResult), this::onFailure);
        return deferredResult;
    }

    private SuccessCallback<ResponseEntity<GitContent>> successHandler(DeferredResult<GitContent> deferredResult) {
        return response -> deferredResult.setResult(response.getBody());
    }

    private void onFailure(Throwable throwable) {
        log.error("error", throwable);
    }
}