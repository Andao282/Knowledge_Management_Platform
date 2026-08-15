package com.oadna.qlsach_api.repository;

import com.oadna.qlsach_api.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,String> {
    List<Category> findByCategoryNameContainingIgnoreCase(String categoryName);
}
