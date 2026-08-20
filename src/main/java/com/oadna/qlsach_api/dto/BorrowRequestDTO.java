package com.oadna.qlsach_api.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowRequestDTO {

    @NotBlank(message = "Không được để trống mã Độc giả!")
    private String readerID;

    @NotBlank(message = "Không được để trống mã Nhân viên")
    private String staffID;

    @NotEmpty(message = "Danh sách mượn không được để trống!")
    private List<String> booksIDs;

}
