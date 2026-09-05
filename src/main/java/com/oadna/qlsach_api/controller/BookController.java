package com.oadna.qlsach_api.controller;

import com.oadna.qlsach_api.dto.BookRequestDTO;
import com.oadna.qlsach_api.dto.BookResponseDTO;
import com.oadna.qlsach_api.entity.Book;
import com.oadna.qlsach_api.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Knowledge_Management_Platform/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping
    public List<BookResponseDTO> getAllBooks(){
        return bookService.getAllBooks();
    }

    @PostMapping
    public BookResponseDTO createBook(@Valid @RequestBody BookRequestDTO books){
        return bookService.addBook(books);
    }

    @PostMapping("/multiple-books")
    public List<BookResponseDTO> createBooks(@Valid @RequestBody List<BookRequestDTO> books){
        return bookService.addBooks(books);
    }

    @PutMapping("/{id}")
    public BookResponseDTO updateBook(@PathVariable String id, @Valid @RequestBody BookRequestDTO bookDetails){
        return bookService.updateBook(id, bookDetails);
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable String id){
        bookService.deleteBook(id);
        return "Delete successfully!";
    }

    @GetMapping("/page")
    public Page<BookResponseDTO> getBooksPaging(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "3") int size,
                                    @RequestParam(defaultValue = "id") String sortBy){
        return bookService.getAllBooksPaging(page, size, sortBy);
    }

    @GetMapping("/search-by-name")
    public List<BookResponseDTO> findBookByName(@RequestParam String name){
        return bookService.findBookByBookNameContainingIgnoreCase(name);
    }

    @GetMapping("/search-by-category")
    public List<BookResponseDTO> findBookByCategoryName(@RequestParam String categoryName){
        return bookService.findBookByCategory_CategoryNameIgnoreCase(categoryName);
    }

    @GetMapping("/{id}")
    public BookResponseDTO getBookById(@PathVariable String id){
        return bookService.getBookById(id);
    }

}
