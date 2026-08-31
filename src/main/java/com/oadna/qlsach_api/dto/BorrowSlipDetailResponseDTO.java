package com.oadna.qlsach_api.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BorrowSlipDetailResponseDTO {
    private String borrowID;
    private String readerID;
    private String staffID;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private Boolean returned;

    private List<BorrowDetailItemDTO> details;
}
