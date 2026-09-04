package com.oadna.qlsach_api.service;

import com.oadna.qlsach_api.entity.Account;
import com.oadna.qlsach_api.entity.Staff;
import com.oadna.qlsach_api.repository.AccountRepository;
import com.oadna.qlsach_api.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private JwtService jwtService;

    public void createAccount(String username, String password, String staffID, String role, String note) {
        Account account = new Account();
        account.setUsername(username);

        String hashedPassword = passwordEncoder.encode(password);
        account.setPassword(hashedPassword);
        account.setCreationDate(LocalDate.now());
        account.setStatus(true);
        account.setNote(note);
        account.setRole(role);

        Staff staff = staffRepository.findById(staffID)
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên có mã " + staffID));
        account.setStaff(staff);
        accountRepository.save(account);
    }

    public String login(String username, String password) {
        Account account = accountRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại!"));

        if(account.getStatus() != null && !account.getStatus()){
            throw new RuntimeException("Tài khoản bị khóa! Liên hệ admin để mở khóa!") ;
        }

        if(!passwordEncoder.matches(password, account.getPassword())){
            throw new RuntimeException("Sai mật khẩu!");
        }

        String token = jwtService.generateToken(account.getUsername(), account.getRole());
        return token;
    }
}
