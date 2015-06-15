package pl.java.scalatech.service.userInformation.impl;

import javax.annotation.Nonnull;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
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
    public User findUserByLogin(String login) {
        return userRepository.findUserByLogin(login);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public User findUserByNip(String nip) {
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

}
