package com.oadna.qlsach_api.controller;

import com.oadna.qlsach_api.entity.Author;
import com.oadna.qlsach_api.entity.Publisher;
import com.oadna.qlsach_api.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Knowledge_Management_Platform/publishers")

public class PublisherController {
    @Autowired
    private PublisherService publisherService;

    @GetMapping
    public List<Publisher> getAllPublishers(){
        return publisherService.getAllPublishers();
    }

    @GetMapping("/{id}")
    public Publisher findPublisherById(@PathVariable String id){
        return publisherService.findById(id);
    }

    @PostMapping
    public List<Publisher> createPublisher(@RequestBody List<Publisher> publishers){
        return publisherService.createPublisher(publishers);
    }

    @PutMapping("/{id}")
    public Publisher updatePublisher(@RequestBody Publisher publisher, @PathVariable String id){
        return publisherService.updatePublisher(id, publisher);
    }

    @DeleteMapping("/{id}")
    public String deletePublisher(@PathVariable String id){
        publisherService.deletePublisher(id);
        return "Xóa NXB thành công!";
    }
}
