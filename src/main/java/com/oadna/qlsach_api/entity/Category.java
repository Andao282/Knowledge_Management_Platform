package com.oadna.qlsach_api.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.Length;

@Entity
@Data
@Table(name = "Category")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Category {
    @Id
    @Column(name = "CategoryID", length = 30)
    private String categoryID;

    @Column(name = "category_name", length = 50)
    private String categoryName;
}
