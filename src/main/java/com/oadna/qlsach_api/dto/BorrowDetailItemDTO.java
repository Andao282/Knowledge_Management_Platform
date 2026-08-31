package com.oadna.qlsach_api.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BorrowDetailItemDTO {
    private String bookID;
    private String bookName;
    private String borrowCondition;
    private String returnCondition;
    private LocalDate expectedReturnDate;
    private LocalDate actualReturnDate;
}
