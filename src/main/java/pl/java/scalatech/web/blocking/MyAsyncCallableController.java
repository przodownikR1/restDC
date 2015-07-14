package pl.java.scalatech.web.blocking;

import java.util.concurrent.Callable;

import javax.annotation.Nonnull;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.java.scalatech.service.my.MyExpensiveService;

@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class MyAsyncCallableController {

    private final @Nonnull MyExpensiveService myExpensiveService;

    @RequestMapping(value = "/callable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Callable<String> executeSlowTask() {
        log.info("Start calculate");
        Callable<String> callable = myExpensiveService::calculate;
        log.info("released thread");
        return callable;
    }
}
