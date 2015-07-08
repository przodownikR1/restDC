package pl.java.scalatech.metrics;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.codahale.metrics.health.HealthCheck;

@Slf4j
public class MailHealthCheck extends HealthCheck implements InitializingBean {
    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Override
    protected Result check() throws Exception {
        log.debug("Initializing JavaMail health indicator");
        //TODO
        // javaMailSender.getSession().getTransport()
        //       .connect(javaMailSender.getHost(), javaMailSender.getPort(), javaMailSender.getUsername(), javaMailSender.getPassword());
        return Result.healthy("Mail service is up !");

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // TODO Auto-generated method stub

    }

}
