package com.oadna.qlsach_api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "Reader")
public class Reader {
    @Id
    @Column(name = "ReaderID", length = 30)
    private String readerID;

    @Column(name = "ReaderName", length = 30)
    private String readerName;

    @Column(name = "Address", length = 30)
    private String address;

    @Column(name = "DOB")
    private LocalDate dob;

    @Column(name = "CardIssueDate")
    private LocalDate cardIssueDate;

    @Column(name = "CardExpiryDate")
    private LocalDate cardExpiryDate;

    @Column(name = "IsPaid")
    private Boolean isPaid;

    @Column(name = "Email", length = 50)
    private String email;

    @Column(name = "Phone", length = 10)
    private String phone;
}
