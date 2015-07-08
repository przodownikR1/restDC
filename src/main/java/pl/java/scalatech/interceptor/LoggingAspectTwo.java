package pl.java.scalatech.interceptor;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspectTwo {

    //@Before("execution (@org.springframework.web.bind.annotation.RequestMapping * *(..))")
    public void logbefore(JoinPoint joinPoint) {
        log.info("+++  logBefore : {}", joinPoint.getSignature().getName());
    }
}