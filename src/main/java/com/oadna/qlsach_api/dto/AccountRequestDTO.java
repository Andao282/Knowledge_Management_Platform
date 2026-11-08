package com.oadna.qlsach_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequestDTO {
    @NotBlank @Size(min = 4, max = 50)
    private String username;

    @NotBlank @Size(min = 6, max = 50)
    private String password;
    private String staffID;

    @NotBlank @Pattern(regexp = "ROLE_ADMIN|ROLE_STAFF")
    private String role;
    private String note;
}
