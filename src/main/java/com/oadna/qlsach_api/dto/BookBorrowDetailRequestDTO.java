package com.oadna.qlsach_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BookBorrowDetailRequestDTO {
    @NotBlank(message = "Không được để trống mã sách!")
    private String bookID;

    @NotBlank(message = "Không được để trống tình trạng sách khi mượn!")
    private String borrowCondition;
}
