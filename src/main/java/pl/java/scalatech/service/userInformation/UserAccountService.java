package pl.java.scalatech.service.userInformation;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import pl.java.scalatech.entity.User;

public interface UserAccountService {

    Optional<User> findUserByLogin(String login);

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByNip(String nip);

    void removeUser(User user);

    User saveUser(User user);

    Page<User> getUsers(Pageable pagable);

}
