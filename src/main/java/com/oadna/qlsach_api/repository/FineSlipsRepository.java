package com.oadna.qlsach_api.repository;

import com.oadna.qlsach_api.entity.BorrowDetails;
import com.oadna.qlsach_api.entity.FineSlips;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FineSlipsRepository extends JpaRepository<FineSlips,String> {
    Optional<FineSlips> findByBorrowDetailsAndIsPaidFalse(BorrowDetails borrowDetails);
}
