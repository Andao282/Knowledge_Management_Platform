package com.oadna.qlsach_api.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "Staff")
public class Staff {
    @Id
    @Column(name = "StaffID", length = 30)
    private String staffID;

    @Column(name = "Name", length = 30)
    private String name;

    @Column(name = "Gender")
    private Boolean gender;

    @Column(name = "DOB")
    private LocalDate dob;

    @Column(name = "Role", length = 30)
    private String role;

    @Column(name = "Phone", length = 10)
    private String phone;
}
