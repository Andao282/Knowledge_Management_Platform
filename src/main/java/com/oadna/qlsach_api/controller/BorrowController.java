package com.oadna.qlsach_api.controller;

import com.oadna.qlsach_api.dto.BorrowRequestDTO;
import com.oadna.qlsach_api.service.BorrowService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<String> createBorrowSlip(@Valid @RequestBody BorrowRequestDTO borrowRequestDTO){
        String message = borrowService.createBorrowSlip(borrowRequestDTO);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }
}
