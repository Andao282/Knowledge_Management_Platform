package com.oadna.qlsach_api.controller;

import com.oadna.qlsach_api.dto.PublisherRequestDTO;
import com.oadna.qlsach_api.dto.PublisherResponseDTO;
import com.oadna.qlsach_api.entity.Author;
import com.oadna.qlsach_api.entity.Publisher;
import com.oadna.qlsach_api.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Knowledge_Management_Platform/publishers")

public class PublisherController {
    @Autowired
    private PublisherService publisherService;

    @GetMapping
    public List<PublisherResponseDTO> getAllPublishers(){
        return publisherService.getAllPublishers();
    }

    @GetMapping("/{id}")
    public Publisher findPublisherById(@PathVariable String id){
        return publisherService.findById(id);
    }

    @GetMapping("/search-by-name")
    public List<PublisherResponseDTO> findPublisherByName(@RequestParam String name){
        return publisherService.findPublisherByPublisherName(name);
    }

    @PostMapping
    public PublisherResponseDTO createPublisher(@RequestBody PublisherRequestDTO publisher){
        return publisherService.createPublisher(publisher);
    }

    @PostMapping("/multiple-publishers")
    public List<PublisherResponseDTO> createPublishers(@RequestBody List<PublisherRequestDTO> publisherRequests){
        return publisherService.createPublishers(publisherRequests);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public PublisherResponseDTO updatePublisher(@RequestBody PublisherRequestDTO publisher, @PathVariable String id){
        return publisherService.updatePublisher(id, publisher);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public String deletePublisher(@PathVariable String id){
        publisherService.deletePublisher(id);
        return "Xóa NXB thành công!";
    }
}
