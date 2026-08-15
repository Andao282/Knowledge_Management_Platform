package com.oadna.qlsach_api.repository;

import com.oadna.qlsach_api.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReaderRepository extends JpaRepository<Publisher,String> {
}
