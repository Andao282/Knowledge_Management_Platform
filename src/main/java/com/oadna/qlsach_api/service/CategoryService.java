package com.oadna.qlsach_api.service;


import com.oadna.qlsach_api.dto.CategoryRequestDTO;
import com.oadna.qlsach_api.entity.Category;
import com.oadna.qlsach_api.dto.CategoryResponseDTO;
import com.oadna.qlsach_api.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryResponseDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Category findById(String id) {
        return categoryRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Mã thể loại không tồn tại!"));
    }

    public List<CategoryResponseDTO> findByName(String name) {
        if(!(name == null) && !name.isEmpty()){
            List<Category> categories = categoryRepository.findByCategoryNameContainingIgnoreCase(name);
        }
        return categoryRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public CategoryResponseDTO updateCategory(String id, CategoryRequestDTO category) {
       Category existingCategory = categoryRepository.findById(id)
               .orElseThrow(() -> new RuntimeException("Không tìm thấy tài liệu có mã: " + id));

       existingCategory.setCategoryName(category.getCategoryName());

       Category updatedCategory = categoryRepository.save(existingCategory);
       return mapToDTO(updatedCategory);
    }

    public void deleteCategory(String id) {
        categoryRepository.deleteById(id);
    }

    public CategoryResponseDTO createCategory(CategoryRequestDTO categories){
        Category category = new Category();
        category.setCategoryID(categories.getCategoryID());
        category.setCategoryName(category.getCategoryName());
        Category savedCategory = categoryRepository.save(category);
        return  mapToDTO(savedCategory);
    }

    public List<CategoryResponseDTO> createCategories(List<CategoryRequestDTO> categories){
        return categories.stream()
                .map(this::createCategory)
                .collect(Collectors.toList());
    }

    private CategoryResponseDTO mapToDTO(Category category) {
        return CategoryResponseDTO.builder()
                .categoryID(category.getCategoryID())
                .categoryName(category.getCategoryName())
                .build();
    }
}
