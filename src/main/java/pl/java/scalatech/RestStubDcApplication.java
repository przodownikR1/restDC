package pl.java.scalatech;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import pl.java.scalatech.config.Metrics2Config;
import pl.java.scalatech.service.userInformation.UserAccountService;
import pl.java.scalatech.util.DataGenerator;

@SpringBootApplication
@Import(Metrics2Config.class)
public class RestStubDcApplication implements CommandLineRunner {
    @Autowired
    private UserAccountService userAccountService;

    public static void main(String[] args) {
        SpringApplication.run(RestStubDcApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        DataGenerator.generateUserData(20).stream().forEach(u -> userAccountService.saveUser(u));
    }
}
