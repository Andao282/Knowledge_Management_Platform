package com.oadna.qlsach_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Data
@Table(name = "Publisher")
public class Publisher {
    @Id
    @Column(name = "PublisherID", length = 30)
    private String publisherID;

    @Column(name = "PublisherName", length = 30)
    private String publisherName;

    @Column(name = "Address", length = 50)
    private String address;

    @Column(name = "Phone", length = 10)
    private String phone;
}
