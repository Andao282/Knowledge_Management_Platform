package com.oadna.qlsach_api.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorResponseDTO {
    private String authorID;
    private String authorName;
    private LocalDate dob;
    private String authorHometown;
    private String penName;
}
