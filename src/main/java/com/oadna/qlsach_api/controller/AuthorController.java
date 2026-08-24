package com.oadna.qlsach_api.controller;


import com.oadna.qlsach_api.dto.AuthorRequestDTO;
import com.oadna.qlsach_api.dto.AuthorResponseDTO;
import com.oadna.qlsach_api.entity.Author;
import com.oadna.qlsach_api.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Knowledge_Management_Platform/authors")

public class AuthorController {
    @Autowired
    private AuthorService authorService;

    @GetMapping
    public List<Author> getAllAuthors(){
        return authorService.getAllAuthors();
    }

    @GetMapping("/{id}")
    public Author getAuthorById(@PathVariable String id){
        return authorService.getAuthorById(id);
    }

    @PostMapping
    public List<Author> createAuthor(@RequestBody List<Author> authors){
        return authorService.createAuthor(authors);
    }

    @DeleteMapping("/{id}")
    public String deleteAuthorById(@PathVariable String id){
        authorService.deleteAuthor(id);
        return "Xóa tác giả thành công!";
    }

    @PutMapping("/{id}")
    public Author updateAuthor(@RequestBody Author author, @PathVariable String id){
        return authorService.updateAuthor(id, author);
    }

    @GetMapping("/search-by-name")
    public List<AuthorResponseDTO> searchAuthorByName(@RequestParam String name){
        return authorService.getAuthorsByName(name);
    }
}
