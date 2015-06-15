package pl.java.scalatech.entity;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

@Slf4j
public class UserBuilderTest {
    @Test
    public void shouldBuilderWork() {
        User user = User.builder().login("przodownik").nip("7944433455").email("przodownikR1@gmail.com").build();
        log.info("{}", user);

    }

}
