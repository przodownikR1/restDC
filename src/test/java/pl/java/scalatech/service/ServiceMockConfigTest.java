package pl.java.scalatech.service;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.transaction.annotation.Transactional;

import pl.java.scalatech.entity.BankAccount;
import pl.java.scalatech.entity.User;
import pl.java.scalatech.exception.UserNotFoundException;
import pl.java.scalatech.repository.BankAccountRepository;
import pl.java.scalatech.repository.UserRepository;
import pl.java.scalatech.service.userInformation.UserAccountService;
import pl.java.scalatech.service.userInformation.impl.UserAccountServiceImpl;

import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class ServiceMockConfigTest {

    @Mock
    private BankAccountRepository bankRepository;
    @Mock
    private UserRepository userRepository;

    private UserAccountService userAccountService;

    private User user;

    @Before
    public void setUp() {
        this.userAccountService = new UserAccountServiceImpl(userRepository, bankRepository);
        user = User
                .builder()
                .email("przodownik@tlen.pl")
                .birthdate(LocalDate.parse("1979-05-03", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .accounts(
                        Lists.newArrayList(BankAccount.builder().amount(new BigDecimal("20")).credit(new BigDecimal("10")).creditCardNumer("3302000343023")
                                .build())).address("Krypska").city("Warsaw").login("przodownik").nip("79044432234").phone("5613234322").passwd("qaz123")
                .build();
    }

    @Test
    public void shouldCreateUser() {
        //given //user         

        //when
        when(userRepository.save(isA(User.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        userAccountService.saveUser(user);
        ArgumentCaptor<User> savedUserArg = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(savedUserArg.capture());
        verifyNoMoreInteractions(userRepository);
        User savedUser = savedUserArg.getValue();
        //then
        assertThat(savedUser.getAccounts()).isEqualTo(
                Lists.newArrayList(BankAccount.builder().amount(new BigDecimal("20")).credit(new BigDecimal("10")).creditCardNumer("3302000343023").build()));
        assertThat(savedUser.getEmail()).isEqualTo("przodownik@tlen.pl");

    }

    @Test(expected = UserNotFoundException.class)
    public void shouldRemovedUserWhenUserNotFoundAndThrows() {
        //given
        User userSave = User
                .builder()
                .email("przodownik@tlen.pl")
                .birthdate(LocalDate.parse("1979-05-03", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .accounts(
                        Lists.newArrayList(BankAccount.builder().amount(new BigDecimal("20")).credit(new BigDecimal("10")).creditCardNumer("3302000343023")
                                .build())).address("Krypska").city("Warsaw").login("test").nip("79044432234").phone("5613234322").passwd("qaz123").build();

        //when
        when(userRepository.findUserByLogin("test")).thenReturn(Optional.empty());
        //then
        userAccountService.removeUser(userSave);
        verify(userRepository, times(1)).delete(userSave);
    }

    @Test
    public void shouldRemovedExistsUser() {
        //given user
        when(userRepository.findUserByLogin("przodownik")).thenReturn(Optional.of(user));
        userAccountService.removeUser(user);
        verify(userRepository, times(1)).delete(user);
    }
}
