package com.oadna.qlsach_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublisherResponseDTO {
    private String publisherID;
    private String publisherName;
    private String publisherAddress;
    private String publisherPhone;
}
