package com.oadna.qlsach_api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StaffResponseDTO {
    private String staffID;
    private String staffName;
    private String role;
}
