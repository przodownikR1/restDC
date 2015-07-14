package pl.java.scalatech.service.my.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import pl.java.scalatech.service.my.MyExpensiveService;

@Service
@Slf4j
public class MyExpensiveServiceImpl implements MyExpensiveService {

    @Override
    @SneakyThrows
    public String calculate() {
        Thread.sleep(5000);
        log.info("calculate ......");
        return "finished";
    }

}
