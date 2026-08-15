package com.oadna.qlsach_api.service;

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

    public List<Category> findAll() {
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

    public Category update(String id, Category category) {
        Category existing = findById(id);
        existing.setCategoryName(category.getCategoryName());
        return categoryRepository.save(existing);
    }

    public void deleteById(String id) {
        categoryRepository.deleteById(id);
    }

    public Category create( Category category) {
        if(categoryRepository.existsById(category.getCategoryID())) {
            throw new RuntimeException("Mã thể loại đã tồn tại");
        }else{
            return categoryRepository.save(category);
        }
    }
}
