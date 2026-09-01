package com.oadna.qlsach_api.repository;

import com.oadna.qlsach_api.entity.BorrowSlips;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowSlipsRepository extends JpaRepository<BorrowSlips,String> {
    List<BorrowSlips> findByReaderID_ReaderID(String readerID);
}
