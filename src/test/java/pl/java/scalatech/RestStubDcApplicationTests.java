package pl.java.scalatech;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
//import static org.assertj.core.api.Assertions.*;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RestStubDcApplication.class)
@WebAppConfiguration
@IntegrationTest(value = "it")

public class RestStubDcApplicationTests {

    @Test
    public void contextLoads() {
    }

}
