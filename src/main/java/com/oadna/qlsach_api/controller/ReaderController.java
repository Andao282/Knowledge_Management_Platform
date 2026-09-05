package com.oadna.qlsach_api.controller;

import com.oadna.qlsach_api.dto.ReaderRequestDTO;
import com.oadna.qlsach_api.dto.ReaderResponseDTO;
import com.oadna.qlsach_api.entity.Reader;
import com.oadna.qlsach_api.service.ReaderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Knowledge_Management_Platform/readers")

public class ReaderController {
    @Autowired
    private ReaderService readerService;

    @GetMapping
    public List<ReaderResponseDTO> getALlReaders(){
        return readerService.getAllReaders();
    }

    @GetMapping("/{id}")
    public ReaderResponseDTO getReaderByID(@PathVariable String id){
        return readerService.getReader(id);
    }

    @PostMapping
    public ReaderResponseDTO createReader(@Valid @RequestBody ReaderRequestDTO reader){
        return readerService.createReader(reader);
    }

    @PostMapping("/multiple-readers")
    public List<ReaderResponseDTO> createReaders(@Valid @RequestBody List<ReaderRequestDTO> readers){
        return readerService.createReaders(readers);
    }

    @PutMapping("/{id}")
    public ReaderResponseDTO updateReader(@PathVariable String id, @Valid @RequestBody ReaderRequestDTO reader){
        return readerService.updateReader(id, reader);
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteReader(@PathVariable String id){
        readerService.deleteReader(id);
        return "Delete successfully!";
    }

    @GetMapping("/{search-by-name}")
    public List<ReaderResponseDTO> getReaderByName(@PathVariable String Name){
        return readerService.getReadersByName(Name);
    }

}
