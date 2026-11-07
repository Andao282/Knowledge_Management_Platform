package com.oadna.qlsach_api.service;

import com.oadna.qlsach_api.entity.Account;
import com.oadna.qlsach_api.entity.Staff;
import com.oadna.qlsach_api.repository.AccountRepository;
import com.oadna.qlsach_api.repository.StaffRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private StaffRepository staffRepository;
    @Mock
    private JwtService jwtService;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountService();
        ReflectionTestUtils.setField(accountService, "accountRepository", accountRepository);
        ReflectionTestUtils.setField(accountService, "staffRepository", staffRepository);
        ReflectionTestUtils.setField(accountService, "passwordEncoder", passwordEncoder);
        ReflectionTestUtils.setField(accountService, "jwtService", jwtService);
    }

    @Test
    void createAccount_hashesPasswordAndLinksStaff() {
        Staff staff = new Staff();
        staff.setStaffID("S01");
        when(staffRepository.findById("S01")).thenReturn(Optional.of(staff));

        accountService.createAccount("an", "secret", "S01", "ROLE_ADMIN", "first account");

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(captor.capture());
        Account saved = captor.getValue();
        assertThat(saved.getUsername()).isEqualTo("an");
        assertThat(saved.getPassword()).isNotEqualTo("secret");
        assertThat(passwordEncoder.matches("secret", saved.getPassword())).isTrue();
        assertThat(saved.getStatus()).isTrue();
        assertThat(saved.getCreationDate()).isEqualTo(LocalDate.now());
        assertThat(saved.getRole()).isEqualTo("ROLE_ADMIN");
        assertThat(saved.getNote()).isEqualTo("first account");
        assertThat(saved.getStaff()).isEqualTo(staff);
    }

    @Test
    void createAccount_throwsWhenStaffDoesNotExist() {
        when(staffRepository.findById("S01")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.createAccount("an", "secret", "S01", "ROLE_ADMIN", null))
                .isInstanceOf(RuntimeException.class);

        verify(accountRepository, never()).save(any());
    }

    @Test
    void login_returnsTokenForValidCredentials() {
        Account account = account("an", "secret", true, "ROLE_ADMIN");
        when(accountRepository.findById("an")).thenReturn(Optional.of(account));
        when(jwtService.generateToken("an", "ROLE_ADMIN")).thenReturn("jwt-token");

        String token = accountService.login("an", "secret");

        assertThat(token).isEqualTo("jwt-token");
    }

    @Test
    void login_throwsWhenAccountDoesNotExist() {
        when(accountRepository.findById("an")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.login("an", "secret"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void login_throwsWhenAccountIsDisabled() {
        Account account = account("an", "secret", false, "ROLE_ADMIN");
        when(accountRepository.findById("an")).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> accountService.login("an", "secret"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void login_throwsWhenPasswordIsWrong() {
        Account account = account("an", "secret", true, "ROLE_ADMIN");
        when(accountRepository.findById("an")).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> accountService.login("an", "wrong-password"))
                .isInstanceOf(RuntimeException.class);
    }

    private Account account(String username, String rawPassword, boolean status, String role) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(passwordEncoder.encode(rawPassword));
        account.setStatus(status);
        account.setRole(role);
        return account;
    }
}
