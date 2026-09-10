package com.oadna.qlsach_api.controller;

import com.oadna.qlsach_api.dto.StaffRequestDTO;
import com.oadna.qlsach_api.dto.StaffResponseDTO;
import com.oadna.qlsach_api.entity.Staff;
import com.oadna.qlsach_api.repository.StaffRepository;
import com.oadna.qlsach_api.service.StaffService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Knowledge_Management_Platform/staffs")

public class StaffController {
    @Autowired
    private StaffService staffService;

    @GetMapping
    public List<StaffResponseDTO> getAllStaffs(){
        return staffService.getAllStaffs();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public StaffResponseDTO createStaff(@Valid @RequestBody StaffRequestDTO staff){
        return staffService.createStaff(staff);
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/multiple-staffs")
    public List<StaffResponseDTO> createMultipleStaffs(@RequestBody List<StaffRequestDTO> staff){
        return staffService.createStaffs(staff);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public StaffResponseDTO updateStaff(@PathVariable String id, @Valid @RequestBody StaffRequestDTO staff){
        return staffService.updateStaff(id, staff);
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteStaff(@PathVariable String id){
        staffService.deleteStaff(id);
        return "Delete successfully!";
    }

    @GetMapping("/{search-by-name}")
    public List<StaffResponseDTO> getStaffsByName(@PathVariable String searchByName){
        return staffService.getStaffsByName(searchByName);
    }


}
