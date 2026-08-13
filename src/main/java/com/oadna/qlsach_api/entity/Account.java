package com.oadna.qlsach_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.TypeAlias;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "Account")
public class Account {
    @Id
    @Column(name = "Username", length = 50)
    private String username;

    @Column(name = "Password", length = 50)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "StaffID")
    private Staff staff;

    @Column(name = "Status")
    private Boolean status;

    @Column(name = "CreationDate")
    private LocalDate creationDate;

    @Column(name = "Note")
    private String note;
}
