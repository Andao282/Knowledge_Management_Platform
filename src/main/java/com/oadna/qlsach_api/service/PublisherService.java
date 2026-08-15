package com.oadna.qlsach_api.service;

import com.oadna.qlsach_api.entity.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublisherRepository {
    @Autowired
    private PublisherRepository publisherRepository;

    public List<Publisher> getAllPublishers(){
        return publisherRepository.
    }
}
