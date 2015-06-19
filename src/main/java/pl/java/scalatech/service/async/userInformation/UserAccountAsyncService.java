package pl.java.scalatech.service.async.userInformation;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.concurrent.ListenableFuture;

import pl.java.scalatech.entity.User;

public interface UserAccountAsyncService {

    ListenableFuture<Optional<User>> findUserByLogin(String login);

    ListenableFuture<Optional<User>> findUserByEmail(String email);

    ListenableFuture<Optional<User>> findUserByNip(String nip);

    ListenableFuture<Void> removeUser(User user);

    ListenableFuture<User> saveUser(User user);

    ListenableFuture<Page<User>> getUsers(Pageable pagable);

}
