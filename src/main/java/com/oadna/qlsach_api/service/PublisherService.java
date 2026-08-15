package com.oadna.qlsach_api.service;

import com.oadna.qlsach_api.entity.Publisher;
import com.oadna.qlsach_api.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublisherService {
    @Autowired
    private PublisherRepository publisherRepository;

    public List<Publisher> getAllPublishers(){
        return publisherRepository.findAll();
    }

    public Publisher findById(String id){
        return publisherRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Không tìm thấy nhà xuất bản!"));
    }

    public Publisher createPublisher(Publisher publisher) {
        if (publisherRepository.existsById(publisher.getPublisherID())) {
            throw new RuntimeException("Nhà xuất bản đã tồn tại!");
        } else {
            return publisherRepository.save(publisher);
        }
    }

    public Publisher updatePublisher(Publisher publisher, String id) {
        Publisher existing = findById(id);
        existing.setPublisherName(publisher.getPublisherName());
        existing.setAddress(publisher.getAddress());
        existing.setPhone(publisher.getPhone());

        return publisherRepository.save(existing);
    }

    public void deletePublisher(String id) {
        Publisher existing = findById(id);
        publisherRepository.delete(existing);
    }

}
