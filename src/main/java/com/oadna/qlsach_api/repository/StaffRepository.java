package com.oadna.qlsach_api.repository;

import com.oadna.qlsach_api.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaffRepository extends JpaRepository<Staff,String> {
    List<Staff> findStaffByNameIgnoreCase(String name);
}
