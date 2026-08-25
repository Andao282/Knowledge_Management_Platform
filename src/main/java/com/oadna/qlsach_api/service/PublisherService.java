package com.oadna.qlsach_api.service;

import com.oadna.qlsach_api.dto.PublisherRequestDTO;
import com.oadna.qlsach_api.dto.PublisherResponseDTO;
import com.oadna.qlsach_api.entity.Category;
import com.oadna.qlsach_api.entity.Publisher;
import com.oadna.qlsach_api.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublisherService {
    @Autowired
    private PublisherRepository publisherRepository;

    public List<PublisherResponseDTO> getAllPublishers(){
        return publisherRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Publisher findById(String id){
        return publisherRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Không tìm thấy nhà xuất bản!"));
    }

    public PublisherResponseDTO createPublisher(PublisherRequestDTO publisherRequest){
        Publisher publisher = new Publisher();

        publisher.setPublisherID(publisherRequest.getPublisherID());
        publisher.setPublisherName(publisherRequest.getPublisherName());
        publisher.setAddress(publisherRequest.getPublisherAddress());
        publisher.setPhone(publisherRequest.getPublisherPhone());

        Publisher savedPublisher = publisherRepository.save(publisher);

        return mapToDTO(savedPublisher);
    }

    public List<PublisherResponseDTO> createPublishers(List<PublisherRequestDTO> publisherRequests){
        return  publisherRequests.stream()
                .map(this::createPublisher)
                .collect(Collectors.toList());
    }

    public PublisherResponseDTO updatePublisher(String id, PublisherRequestDTO publisher) {
        Publisher existing = findById(id);
        existing.setPublisherName(publisher.getPublisherName());
        existing.setAddress(publisher.getPublisherAddress());
        existing.setPhone(publisher.getPublisherPhone());

        Publisher updatedPublisher = publisherRepository.save(existing);

        return mapToDTO(updatedPublisher);
    }

    public List<PublisherResponseDTO> findPublisherByPublisherName(String publisherName){
        List<Publisher> publishers = publisherRepository.findPublisherByPublisherNameIgnoreCase(publisherName);
        return publishers.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public void deletePublisher(String id) {
        if(!publisherRepository.existsById(id)){
            throw new RuntimeException("Không tìm thấy NXB có mã: " + id);
        }
        publisherRepository.deleteById(id);
    }

    private PublisherResponseDTO mapToDTO(Publisher publisher){
        return PublisherResponseDTO.builder()
                .publisherID(publisher.getPublisherID())
                .publisherName(publisher.getPublisherName())
                .publisherAddress(publisher.getAddress())
                .publisherPhone(publisher.getPhone())
                .build();
    }


}
