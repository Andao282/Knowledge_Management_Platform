package com.oadna.qlsach_api.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "BorrowSlips")
public class BorrowSlips {
    @Id
    @Column(name = "BorrowID", length = 50)
    private String borrowID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ReaderID")
    private Reader readerID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "StaffID")
    private Staff staffID;

    @Column(name = "BorrowDate")
    private LocalDate borrowDate;

    @Column(name = "DueDate")
    private LocalDate dueDate;

    @Column(name = "Return")
    private Boolean returned;

}
