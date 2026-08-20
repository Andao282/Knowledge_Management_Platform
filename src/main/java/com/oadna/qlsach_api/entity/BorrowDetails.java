package com.oadna.qlsach_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@Table(name = "BorrowDetails")
public class BorrowDetails {
    @EmbeddedId
    private BorrowDetailsID id;

    @ManyToOne
    @MapsId("borrowID")
    @JoinColumn(name = "BorrowID")
    private BorrowSlips borrowSlips;

    @ManyToOne
    @MapsId("bookID")
    @JoinColumn(name = "BookID")
    private Book book;

    @Column(name = "BorrowCondition", length = 20)
    private String borrowCondition;

    @Column(name = "ExpectedReturnDate")
    private LocalDate expectedReturnDate;

    @Column(name = "ActualReturnDate")
    private LocalDate actualReturnDate;

    @Column(name = "ReturnCondition", length = 20)
    private String returnCondition;

}
