package pl.java.scalatech.service.async.userInformation.impl;

import java.util.Optional;

import javax.annotation.Nonnull;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;

import pl.java.scalatech.entity.User;
import pl.java.scalatech.repository.BankAccountRepository;
import pl.java.scalatech.repository.UserRepository;
import pl.java.scalatech.service.async.userInformation.UserAccountAsyncService;

@Service
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@Transactional(readOnly = true)
public class UserAccountAsyncServiceImpl implements UserAccountAsyncService {

    @Getter
    private final @NonNull UserRepository userRepository;
    @Getter
    private final @Nonnull BankAccountRepository bankAccountRepository;

    @Override
    @Async
    public ListenableFuture<Optional<User>> findUserByLogin(String login) {
        return new AsyncResult<>(userRepository.findUserByLogin(login));
    }

    @Override
    @Async
    public ListenableFuture<Optional<User>> findUserByEmail(String email) {
        return new AsyncResult<>(userRepository.findUserByEmail(email));
    }

    @Override
    @Async
    public ListenableFuture<Optional<User>> findUserByNip(String nip) {
        return new AsyncResult<>(userRepository.findUserByNip(nip));
    }

    @Override
    @Async
    @Transactional
    public ListenableFuture<Void> removeUser(User user) {
        // TODO userRespository.delete(user);
        return new AsyncResult<Void>(null);
    }

    @Override
    @Async
    @Transactional
    public ListenableFuture<User> saveUser(User user) {
        return new AsyncResult<>(userRepository.save(user));
    }

    @Override
    @Async
    public ListenableFuture<Page<User>> getUsers(Pageable pageable) {
        return new AsyncResult<>(userRepository.findAll(pageable));
    }

}
