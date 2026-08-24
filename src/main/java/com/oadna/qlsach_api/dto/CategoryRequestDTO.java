package com.oadna.qlsach_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDTO {
    @NotBlank(message = "Không được để trống mã thể loại")
    private String categoryID;

    @NotBlank(message = "Không được để trống tên thể loại")
    private String categoryName;
}
