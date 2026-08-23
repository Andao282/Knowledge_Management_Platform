package com.oadna.qlsach_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorRequestDTO {
    @NotBlank(message = "Không được để trống mã tác giả!")
    private String authorID;

    @NotBlank(message = "Không được để trống tên tác giả!")
    private String authorName;

    @NotBlank(message = "Không được để trống ngày sinh!")
    private LocalDate dob;

    @NotBlank(message = "Không được để trống quê quán!")
    private String authorHometown;

    private String penName;

}
