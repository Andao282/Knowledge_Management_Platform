package com.oadna.qlsach_api.repository;

import com.oadna.qlsach_api.entity.Reader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReaderRepository extends JpaRepository<Reader,String> {
    List<Reader> findReaderByReaderNameIgnoreCase(String readerName);
}
