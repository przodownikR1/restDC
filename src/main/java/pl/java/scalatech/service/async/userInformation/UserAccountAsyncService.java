package pl.java.scalatech.service.async.userInformation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.concurrent.ListenableFuture;

import pl.java.scalatech.entity.User;

public interface UserAccountAsyncService {

    ListenableFuture<User> findUserByLogin(String login);

    ListenableFuture<User> findUserByEmail(String email);

    ListenableFuture<User> findUserByNip(String nip);

    ListenableFuture<Void> removeUser(User user);

    ListenableFuture<User> saveUser(User user);

    ListenableFuture<Page<User>> getUsers(Pageable pagable);

}
