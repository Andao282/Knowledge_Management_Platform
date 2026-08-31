package com.oadna.qlsach_api.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BorrowResponseDTO {
    private String borrowID;
    private String readerID;
    private String staffID;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private Boolean returned;
}
