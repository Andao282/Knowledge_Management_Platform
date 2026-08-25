package com.oadna.qlsach_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.bridge.IMessage;

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

    @NotNull(message = "Không được để trống số lượng!")
    @Min(value = 0, message = "Số lượng sách tối thiểu là 0!")
    private Integer quantity;

}
