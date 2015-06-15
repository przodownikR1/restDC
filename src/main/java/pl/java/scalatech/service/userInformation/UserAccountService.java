package pl.java.scalatech.service.userInformation;

import pl.java.scalatech.entity.User;

public interface UserAccountService {

    User findUserByLogin(String login);

    User findUserByEmail(String email);

    User findUserByNip(String nip);

    void removeUser(User user);

    User saveUser(User user);

}
