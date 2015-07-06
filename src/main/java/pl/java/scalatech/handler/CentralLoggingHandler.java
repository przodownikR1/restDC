package pl.java.scalatech.handler;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@Aspect
@Component
@Slf4j
public class CentralLoggingHandler {

    @Before("@annotation(org.springframework.web.bind.annotation.RequestMapping) && @annotation(mapping)")
    public void logControllerAccess(RequestMapping mapping) {
        log.info("Executing {} request", mapping.value()[0]);
    }

    @Before("execution(* pl.java.scalatech.service.*..*Service+.*(..)) && target(service)")
    public void logServiceAccess(Object service) {
        log.info("Accessing {}", service.getClass().getSimpleName());
    }
}