package com.oadna.qlsach_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffRequestDTO {
    @NotBlank(message = "Không được để trống mã nhân viên!")
    private String staffID;

    @NotBlank(message = "Không được để trống tên!")
    private String staffName;

    @NotBlank(message = "Số điện thoại không được để trống!")
    @Pattern(regexp = "^\\d{10}$", message = "Số điện thoại không hợp lệ (phải gồm 10 số)")
    private String phone;

    @NotBlank(message = "Chức vụ(Role) không được để trống!")
    private String role;
}
