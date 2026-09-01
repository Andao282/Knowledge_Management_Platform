package com.oadna.qlsach_api.controller;

import com.oadna.qlsach_api.dto.BorrowRequestDTO;
import com.oadna.qlsach_api.dto.BorrowResponseDTO;
import com.oadna.qlsach_api.dto.BorrowSlipDetailResponseDTO;
import com.oadna.qlsach_api.dto.ReturnRequestDTO;
import com.oadna.qlsach_api.entity.BorrowSlips;
import com.oadna.qlsach_api.service.BorrowService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/return")
    public ResponseEntity<String> returnBooks(@Valid @RequestBody ReturnRequestDTO returnRequestDTO){
        String message = borrowService.returnBooks(returnRequestDTO);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BorrowResponseDTO>> getAllBorrowSlips(){
        List<BorrowResponseDTO> slipsList = borrowService.getAllBorrowSlips();
        return new ResponseEntity<>(slipsList, HttpStatus.OK);
    }

    @GetMapping("/{borrowID}")
    public ResponseEntity<BorrowSlipDetailResponseDTO> getBorrowById(@PathVariable String borrowID){
        BorrowSlipDetailResponseDTO result = borrowService.getBorrowSlipByID(borrowID);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/reader/{readerID}")
    public ResponseEntity<?> getBorrowByReaderID(@PathVariable String readerID){
        List<BorrowResponseDTO> borrowHistory = borrowService.getBorrowHistoryByReader(readerID);

        if(borrowHistory.isEmpty()){
            return ResponseEntity.ok("Không có lịch sử mượn sách!");
        }

        return new ResponseEntity<>(borrowHistory, HttpStatus.OK);
    }
}
