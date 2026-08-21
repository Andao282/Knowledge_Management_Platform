package com.oadna.qlsach_api.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class StaffResponseDTO {
    private String staffID;
    private String staffName;
    private String role;
    private LocalDate dob;
    private String phone;
}
