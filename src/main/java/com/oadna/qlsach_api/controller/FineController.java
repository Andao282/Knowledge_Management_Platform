package com.oadna.qlsach_api.controller;

import com.oadna.qlsach_api.service.FineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("Knowledge_Management_Platform/fines")
public class FineController {
    @Autowired
    private FineService fineService;

    @PutMapping("/{fineID}/pay")
    public ResponseEntity<String> payFine(@PathVariable("fineID") String fineID){
        String result = fineService.payFine(fineID);
        return ResponseEntity.ok(result);
    }
}
