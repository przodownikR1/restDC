package pl.java.scalatech.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.java.scalatech.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByLogin(String login);

    User findUserByEmail(String email);

    User findUserByNip(String nip);
}
