package pl.java.scalatech.aop;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.codahale.metrics.MetricRegistry;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Autowired
    private Environment env;

    @Autowired
    MetricRegistry registry;

    @Pointcut("within(pl.java.scalatech.repository..*) || within(pl.java.scalatech.service..*) || within(pl.java.scalatech.web..*)")
    public void loggingPoincut() {
    }

    @AfterThrowing(pointcut = "loggingPoincut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        log.error("^^^  Exception in {}.{}() with cause = {}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(),
                e.getCause(), e);
    }

    @Pointcut("execution(public * *(..))")
    public void publicMethod() {

    }
}