package com.oadna.qlsach_api.service;


import com.oadna.qlsach_api.entity.Staff;
import com.oadna.qlsach_api.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffService {
    @Autowired
    private StaffRepository staffRepository;

    public List<Staff> findAll() {
        return staffRepository.findAll();
    }

    public Staff findById(String id) {
        return staffRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Mã nhân viên không tồn tại!"));
    }

    public Staff create(Staff staff) {
        return staffRepository.save(staff);
    }

    public Staff update(Staff staff, String id) {
        Staff existing = findById(id);
        existing.setName(staff.getName());
        existing.setDob(staff.getDob());
        existing.setGender(staff.getGender());
        existing.setLevel(staff.getLevel());
        existing.setPhone(staff.getPhone());

        return staffRepository.save(existing);
    }

    public void delete(String id) {
        Staff existing = findById(id);
        staffRepository.delete(existing);
    }
}
