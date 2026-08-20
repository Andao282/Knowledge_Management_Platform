package com.oadna.qlsach_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReaderRequestDTO {
    @NotBlank(message = "Không được để trống mã độc giả!")
    private String readerID;

    @NotBlank(message = "Không được để trống tên độc giả!")
    private String readerName;

    @NotBlank(message = "Không được để trống địa chỉ!")
    private String readerAddress;

    @NotBlank(message = "Số điện thoại không được để trống!")
    @Pattern(regexp = "^\\d{10}$", message = "Số điện thoại không hợp lệ (phải gồm 10 số)")
    private String phone;

    @NotBlank(message = "Email không được để trống!")
    @Email(message = "Email không đúng định dạng (ví dụ hợp lệ: abc@gmail.com)!")
    private String email;

    @NotNull(message = "Ngày sinh không được để trống!")
    private LocalDate dob;
}
