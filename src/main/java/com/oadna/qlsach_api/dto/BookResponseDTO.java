package com.oadna.qlsach_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponseDTO {
    private String bookID;
    private String bookName;
    private String authorName;
    private String categoryName;
    private String publisherName;
}
