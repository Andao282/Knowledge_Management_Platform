package com.oadna.qlsach_api.service;

import com.oadna.qlsach_api.dto.StaffRequestDTO;
import com.oadna.qlsach_api.dto.StaffResponseDTO;
import com.oadna.qlsach_api.entity.Book;
import com.oadna.qlsach_api.entity.Staff;
import com.oadna.qlsach_api.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffService {

    @Autowired
    private StaffRepository staffRepository;

    public List<StaffResponseDTO> getAllStaffs() {
        return staffRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Staff findById(String id) {
        return staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên với mã: " + id));
    }

    public StaffResponseDTO createStaff(StaffRequestDTO request) {
        if (staffRepository.existsById(request.getStaffID())) {
            throw new RuntimeException("Mã nhân viên đã tồn tại!");
        }

        Staff staff = new Staff();
        staff.setStaffID(request.getStaffID());
        staff.setStaffName(request.getStaffName());
        staff.setRole(request.getRole());
        staff.setDob(request.getDob());
        staff.setPhone(request.getPhone());

        Staff savedStaff = staffRepository.save(staff);
        return mapToDTO(savedStaff);
    }

    @Transactional
    public List<StaffResponseDTO> createStaffs(List<StaffRequestDTO> requests) {
        return requests.stream()
                .map(this::createStaff)
                .collect(Collectors.toList());
    }

    public List<StaffResponseDTO> getStaffsByName (String searchByName) {
        List<Staff> staffs = staffRepository.findStaffByStaffNameIgnoreCase(searchByName);

        return staffs.stream()
                .map(this :: mapToDTO)
                .collect(Collectors.toList());
    }

    public StaffResponseDTO updateStaff(String id, StaffRequestDTO request) {
        Staff existingStaff = findById(id);

        existingStaff.setStaffName(request.getStaffName());
        existingStaff.setRole(request.getRole());
        existingStaff.setDob(request.getDob());
        existingStaff.setPhone(request.getPhone());

        Staff updatedStaff = staffRepository.save(existingStaff);
        return mapToDTO(updatedStaff);
    }

    public void deleteStaff(String id) {
        Staff existingStaff = findById(id);
        staffRepository.delete(existingStaff);
    }

    private StaffResponseDTO mapToDTO(Staff staff) {
        return StaffResponseDTO.builder()
                .staffID(staff.getStaffID())
                .staffName(staff.getStaffName())
                .role(staff.getRole())
                .dob(staff.getDob())
                .phone(staff.getPhone())
                .build();
    }
}