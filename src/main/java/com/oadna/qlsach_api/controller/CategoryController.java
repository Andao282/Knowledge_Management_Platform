package com.oadna.qlsach_api.controller;

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
    public List<Category> getAllCategories(){
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public Category findCategoryById(@PathVariable String id){
        return categoryService.findById(id);
    }

    @PostMapping
    public List<Category> createCategory(@RequestBody List<Category> categories){
        return categoryService.createCategory(categories);
    }


    @PutMapping("/{id}")
    public Category updateCategory(@RequestBody Category category, @PathVariable String id){
        return categoryService.updateCategory(id, category);
    }

    @DeleteMapping("/{id}")
    public String deleteCategory(@PathVariable String id){
        categoryService.deleteCategory(id);
        return "Xóa Thể loại thành công!";
    }

    @GetMapping("/{search}")
    public List<Category> findCategoryByName(@RequestParam String name){
        return categoryService.findByName(name);
    }
}
