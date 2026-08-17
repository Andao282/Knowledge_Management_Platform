package com.oadna.qlsach_api.controller;

import com.oadna.qlsach_api.entity.Book;
import com.oadna.qlsach_api.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Book_Manage_System/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping
    public List<Book> getAllBooks(){
        return bookService.getAllBooks();
    }

    @PostMapping
    public List<Book> createBook(@Valid @RequestBody List<Book> books){
        return bookService.addBook(books);
    }

    @PutMapping("/{id}")
    public Book updateBook(@PathVariable String id, @Valid @RequestBody Book bookDetails){
        return bookService.updateBook(id, bookDetails);
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable String id){
        bookService.deleteBook(id);
        return "Delete successfully!";
    }

    @GetMapping("/page")
    public Page<Book> getBooksPaging(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "3") int size,
                                    @RequestParam(defaultValue = "id") String sortBy){
        return bookService.getAllBooksPaging(page, size, sortBy);
    }

    public List<Book> findBookByName(@RequestParam String name){
        return bookService.findBookByNameIgnoreCase(name);
    }

    public List<Book> findBookByCategoryName(@RequestParam String categoryName){
        return bookService.findBookByCategory_CategoryNameIgnoreCase(categoryName);
    }

}
