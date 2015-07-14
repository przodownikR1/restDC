package pl.java.scalatech.web.blocking;

import java.util.Random;
import java.util.concurrent.Callable;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping("/blockTest")
@Slf4j
public class RestService {
    static Random random = new Random();

    @RequestMapping(value = "/sync", method = RequestMethod.GET)
    public String sync() {
        log.info("start sync");
        longProcess();
        log.info("end sync");
        return "ok " + random.nextInt(100);
    }

    @RequestMapping(value = "/callable", method = RequestMethod.GET)
    public Callable<String> async() {
        log.info("start callable");
        return () -> {
            longProcess();
            log.info("end callable");
            return "ok " + random.nextInt(100);
        };
    }

    @RequestMapping(value = "/deferred", method = RequestMethod.GET)
    public DeferredResult<String> deffered() {
        final DeferredResult<String> deferredResult = new DeferredResult<>();
        // Save the deferredResult in in-memory queue ...
        new Thread(() -> {
            log.info("start deffered");
            longProcess();
            deferredResult.setResult("ok " + random.nextInt(100));

        }).start();

        return deferredResult;
    }

    @SneakyThrows
    void longProcess() {
        Thread.sleep(1000);
    }
}