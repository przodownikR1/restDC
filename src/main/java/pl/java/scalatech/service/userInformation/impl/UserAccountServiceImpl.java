package pl.java.scalatech.service.userInformation.impl;

import java.util.Optional;

import javax.annotation.Nonnull;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.java.scalatech.entity.User;
import pl.java.scalatech.repository.BankAccountRepository;
import pl.java.scalatech.repository.UserRepository;
import pl.java.scalatech.service.userInformation.UserAccountService;

@Service
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@Transactional(readOnly = true)
public class UserAccountServiceImpl implements UserAccountService {
    @Getter
    private final @NonNull UserRepository userRepository;
    @Getter
    private final @Nonnull BankAccountRepository bankAccountRepository;

    @Override
    public Optional<User> findUserByLogin(String login) {
        return userRepository.findUserByLogin(login);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public Optional<User> findUserByNip(String nip) {
        return userRepository.findUserByNip(nip);
    }

    @Override
    @Transactional
    public void removeUser(User user) {
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

}
