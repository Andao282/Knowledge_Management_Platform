package com.oadna.qlsach_api.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ReaderResponseDTO {
    private String readerID;
    private String readerName;
    private String readerAddress;
    private String email;
    private LocalDate dob;
    private LocalDate cardIssueDate;
    private LocalDate cardExpiryDate;
    private Boolean isPaid;
}
