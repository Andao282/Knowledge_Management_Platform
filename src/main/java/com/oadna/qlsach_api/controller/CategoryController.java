package com.oadna.qlsach_api.controller;

import com.oadna.qlsach_api.dto.CategoryRequestDTO;
import com.oadna.qlsach_api.dto.CategoryResponseDTO;
import com.oadna.qlsach_api.entity.Category;
import com.oadna.qlsach_api.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Knowledge_Management_Platform/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<CategoryResponseDTO> getAllCategories(){
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public Category findCategoryById(@PathVariable String id){
        return categoryService.findById(id);
    }

    @PostMapping
    public CategoryResponseDTO createCategory(@RequestBody CategoryRequestDTO category){
        return categoryService.createCategory(category);
    }

    @PostMapping("/multiple-categories")
    public List<CategoryResponseDTO> createMultipleCategories(@RequestBody List<CategoryRequestDTO> category){
        return categoryService.createCategories(category);
    }


    @PutMapping("/{id}")
    public CategoryResponseDTO updateCategory(@RequestBody CategoryRequestDTO category, @PathVariable String id){
        return categoryService.updateCategory(id, category);
    }

    @DeleteMapping("/{id}")
    public String deleteCategory(@PathVariable String id){
        categoryService.deleteCategory(id);
        return "Xóa Thể loại thành công!";
    }

    @GetMapping("/{search}")
    public List<CategoryResponseDTO> findCategoryByName(@RequestParam String name){
        return categoryService.findByName(name);
    }
}
