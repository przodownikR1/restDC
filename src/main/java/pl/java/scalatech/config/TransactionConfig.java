package pl.java.scalatech.config;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "pl.java.scalatech.repository")
@EntityScan(basePackages = "pl.java.scalatech.entity")
@PropertySource("classpath:application.properties")
@EnableTransactionManagement
@EnableAutoConfiguration
@ComponentScan(basePackages = { "pl.java.scalatech.service", "pl.java.scalatech.repository" })
@Slf4j
public class TransactionConfig {

}
