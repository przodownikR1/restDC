package pl.java.scalatech.web.blocking;

import static java.util.concurrent.CompletableFuture.supplyAsync;

import javax.annotation.Nonnull;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import pl.java.scalatech.service.my.MyExpensiveService;

@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class MyAsyncDeferredController {
    private final @Nonnull MyExpensiveService myExpensiveService;

    @RequestMapping(value = "/deferred", method = RequestMethod.GET, produces = "text/html")
    public DeferredResult<String> executeSlowTask() {
        log.info("Start calculate");
        DeferredResult<String> deferredResult = new DeferredResult<>();
        supplyAsync(myExpensiveService::calculate).whenCompleteAsync((result, throwable) -> deferredResult.setResult(result));
        log.info("released thread");

        return deferredResult;
    }
}
