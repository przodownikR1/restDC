package pl.java.scalatech.config;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import pl.java.scalatech.aop.LoggingAspect;

@Configuration
@EnableAspectJAutoProxy
@Slf4j
@Profile("dev")
@ComponentScan(basePackages = { "pl.java.scalatech.aop", "pl.java.scalatech.interceptor", "pl.java.scalatech.handler" }, useDefaultFilters = false, includeFilters = {
        @Filter(Aspect.class), @Filter(Component.class) })
public class AopConfig {
    @Bean
    // @Profile("dev")
    public LoggingAspect loggingAspect() {
        log.info("!!--------------------------------------------------------------------------loggingAspect ");
        return new LoggingAspect();
    }
}
