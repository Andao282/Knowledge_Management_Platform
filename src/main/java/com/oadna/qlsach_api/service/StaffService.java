package com.oadna.qlsach_api.service;


import com.oadna.qlsach_api.dto.ReaderResponseDTO;
import com.oadna.qlsach_api.dto.StaffResponseDTO;
import com.oadna.qlsach_api.entity.Staff;
import com.oadna.qlsach_api.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffService {
    @Autowired
    private StaffRepository staffRepository;

    public List<StaffResponseDTO> getAllStaffs(){
        return staffRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public StaffResponseDTO createStaff(StaffResponseDTO staffResponseDTO){
        Staff staff = new Staff();
        staff.setStaffID(staffResponseDTO.getStaffID());
        staff.setName(staffResponseDTO.getStaffName());
        staff.setDob(staffResponseDTO.getDob());
        staff.setRole(staffResponseDTO.getRole());
        staff.setPhone(staffResponseDTO.getPhone());

        Staff savedStaff = staffRepository.save(staff);

        return mapToDTO(staffRepository.save(savedStaff));
    }

    public List<StaffResponseDTO> createStaffs(List<StaffResponseDTO> staffResponseDTO){
        return staffResponseDTO.stream()
                .map(this::createStaff)
                .collect(Collectors.toList());
    }

    private StaffResponseDTO mapToDTO(Staff staff) {
        return StaffResponseDTO.builder()
                .staffID(staff.getStaffID())
                .staffName(staff.getName())
                .role(staff.getRole())
                .build();
    }

    public StaffResponseDTO updateStaff(String id, StaffResponseDTO staffRequestDTO) {
        Staff existingStaff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff with id " + id + " not found"));

        existingStaff.setName(staffRequestDTO.getStaffName());
        existingStaff.setDob(staffRequestDTO.getDob());
        existingStaff.setRole(staffRequestDTO.getRole());
        existingStaff.setPhone(staffRequestDTO.getPhone());

        Staff updatedStaff = staffRepository.save(existingStaff);

        return mapToDTO(updatedStaff);
    }

    public void deleteStaff(String id) {
        if(!staffRepository.existsById(id)){
            throw new RuntimeException("Staff with id " + id + " not found");
        }
        staffRepository.deleteById(id);
    }

    public List<StaffResponseDTO> getStaffsByName(String name){
        List<Staff> staffs = staffRepository.findStaffByNameIgnoreCase(name);

        return staffs.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}
