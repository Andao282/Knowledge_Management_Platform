package com.oadna.qlsach_api.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    @Valid
    private List<BookBorrowDetailRequestDTO> details;

    @NotBlank(message = "Không được để trống mã phiếu mượn!")
    private String borrowID;

    @NotNull(message = "Không được để trống ngày mượn!")
    private LocalDate borrowDate;

    @NotNull(message = "Hạn trả không được để trống!")
    @Future(message = "Hạn trả phải là một ngày trong tương lai!")
    private LocalDate dueDate;



}
