package com.oadna.qlsach_api.service;

import com.oadna.qlsach_api.entity.Author;
import com.oadna.qlsach_api.entity.Category;
import com.oadna.qlsach_api.repository.BorrowDetailsRepository;
import com.oadna.qlsach_api.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category findById(String id) {
        return categoryRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Mã thể loại không tồn tại!"));
    }

    public List<Category> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return categoryRepository.findAll();
        } else {
            return categoryRepository.findByCategoryNameContainingIgnoreCase(name);
        }
    }

    public Category updateCategory(String id, Category category) {
        Category existing = findById(id);
        existing.setCategoryName(category.getCategoryName());
        return categoryRepository.save(existing);
    }

    public void deleteCategory(String id) {
        categoryRepository.deleteById(id);
    }

    public List<Category> createCategory(List<Category> categories){
        for (Category category : categories) {
            if (categoryRepository.existsById(category.getCategoryID())) {
                throw new RuntimeException("Mã thể loại đã tồn tại!");
            }
        }
        return categoryRepository.saveAll(categories);
    }
}
