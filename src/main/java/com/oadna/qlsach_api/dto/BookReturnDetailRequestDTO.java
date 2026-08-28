package com.oadna.qlsach_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BookReturnDetailRequestDTO {
    @NotBlank(message = "Không được để trống mã sách!")
    private String bookID;

    @NotBlank(message = "Tình trạng sách khi trả không được để trống!")
    private String returnCondition;
}
