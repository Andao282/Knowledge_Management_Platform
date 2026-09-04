package com.oadna.qlsach_api.controller;

import com.oadna.qlsach_api.dto.AccountRequestDTO;
import com.oadna.qlsach_api.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/Knowledge_Management_Platform/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

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
    public String login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        String token = accountService.login(username, password);
        return token;
    }
}
