package com.oadna.qlsach_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublisherRequestDTO {
    @NotBlank(message = "Không được để trống mã NXB!")
    private String publisherID;

    @NotBlank(message = "Không được để trống tên NXB!")
    private String publisherName;

    @NotBlank(message = "Không được để trống địa chỉ!")
    private String publisherAddress;

    @NotBlank(message = "Không được để trống SĐT!")
    @Pattern(regexp = "^\\d{10}$", message = "Số điện thoại không hợp lệ (phải gồm 10 số)")
    private String publisherPhone;
}
