package com.oadna.qlsach_api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

import java.time.LocalDate;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Data
@Table(name = "Author")
public class Author {
    @Id
    @Column(name = "AuthorID", length = 30)
    private String authorID;

    @Column(name = "AuthorName", length = 30)
    private String authorName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "DOB")
    private LocalDate dob;

    @Column(name = "AuthorHometown", length = 30)
    private String authorHometown;

    @Column(name = "PenName", length = 30)
    private String penName;
}
