package com.oadna.qlsach_api.controller;

import com.oadna.qlsach_api.dto.AccountRequestDTO;
import com.oadna.qlsach_api.dto.LoginRequest;
import com.oadna.qlsach_api.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/Knowledge_Management_Platform/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/create")
    public String createAccount(@RequestBody AccountRequestDTO account) {
         accountService.createAccount(
                account.getUsername(),
                account.getPassword(),
                account.getStaffID(),
                account.getRole(),
                account.getNote()
        );
         return "Tạo tài khoản thành công!";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest login) {
        String username = login.getUsername();
        String password = login.getPassword();

        String token = accountService.login(username, password);
        return ResponseEntity.ok(token);
    }
}
