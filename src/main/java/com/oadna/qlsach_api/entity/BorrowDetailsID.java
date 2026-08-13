package com.oadna.qlsach_api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class BorrowDetailsID implements Serializable {
    @Column(name = "BorrowID", length = 50)
    private String borrowID;

    @Column(name = "BookID", length = 50)
    private String bookID;
}
