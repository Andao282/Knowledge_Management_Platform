package com.oadna.qlsach_api.repository;

import com.oadna.qlsach_api.entity.BorrowDetails;
import com.oadna.qlsach_api.entity.BorrowDetailsID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowDetailsRepository extends JpaRepository<BorrowDetails, BorrowDetailsID> {
    List<BorrowDetails> findById_BorrowID(String borrowID);
}
