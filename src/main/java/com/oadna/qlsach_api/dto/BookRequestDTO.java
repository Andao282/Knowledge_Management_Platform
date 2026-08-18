package com.oadna.qlsach_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookRequestDTO {
    @NotBlank(message = "Không được để trống mã sách!")
    private String bookID;

    @NotBlank(message = "Không được để trống tên sách!")
    private String bookName;

    @NotBlank(message = "Không được để trống mã tác giả!")
    private String authorID;

    @NotBlank(message = "Không được để trống mã danh mục!")
    private String categoryID;

    @NotBlank(message = "Không được để trống mã NXB")
    private String publisherID;

}
