package com.oadna.qlsach_api.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "FineSlips")
public class FineSlips {
    @Id
    @Column(name = "FineID", length = 50)
    private String fineID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "BorrowID", referencedColumnName = "BorrowID"),
            @JoinColumn(name = "BookID", referencedColumnName = "BookID")
    })
    private BorrowDetails borrowDetails;

    @Column(name = "FineDate")
    private LocalDate fineDate;

    @Column(name = "FineReason")
    private String fineReason;

    @Column(name = "FineAmount", length = 200)
    private Double fineAmount;

    @Column(name = "IsPaid")
    private Boolean isPaid;
}
