package com.oadna.qlsach_api.repository;

import com.oadna.qlsach_api.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublisherRepository extends JpaRepository<Publisher,String> {
    List<Publisher> findPublisherByPublisherNameIgnoreCase(String name);
}
