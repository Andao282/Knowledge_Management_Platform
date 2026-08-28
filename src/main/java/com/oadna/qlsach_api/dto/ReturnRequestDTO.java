package com.oadna.qlsach_api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class ReturnRequestDTO {
    @NotBlank(message = "Không được để trống mã phiếu mượn!")
    private String borrowID;

    @NotEmpty(message = "Danh sách trả không được để trống!")
    @Valid
    private List<BookReturnDetailRequestDTO> details;
}
