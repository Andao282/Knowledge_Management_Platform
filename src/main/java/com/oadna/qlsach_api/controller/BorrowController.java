package com.oadna.qlsach_api.controller;

import com.oadna.qlsach_api.dto.BorrowRequestDTO;
import com.oadna.qlsach_api.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("Knowledge_Management_Platform/borrows")
public class BorrowController {
    @Autowired
    private BorrowService borrowService;

    @PostMapping
    public ResponseEntity<String> createBorrowSlip(@RequestBody BorrowRequestDTO borrowRequestDTO){
        String message = borrowService.createBorrowSlip(borrowRequestDTO);
        return ResponseEntity.ok(message);
    }
}
