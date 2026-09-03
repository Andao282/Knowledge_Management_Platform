package com.oadna.qlsach_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequestDTO {
    private String username;
    private String password;
    private String staffID;
    private String role;
    private String note;
}
