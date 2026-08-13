package com.oadna.qlsach_api.service;

import com.oadna.qlsach_api.entity.Book;
import com.oadna.qlsach_api.repository.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private BooksRepository booksRepository;

    //Lay API toan bo sach
    public List<Book> getAllBooks(){
        return booksRepository.findAll();
    }

    //API them sach
    public Book addBook(Book book){
        return booksRepository.save(book);
    }

    public Book updateBook(String id, Book bookDetails){
        Book existedBook = booksRepository.findById(id).orElseThrow(() -> new RuntimeException("Book with id " + id + " not found"));

        //Lay du lieu moi (bookDetails) gan len du lieu cu
        existedBook.setBookName(bookDetails.getBookName());
        existedBook.setAuthor(bookDetails.getAuthor());
        existedBook.setCategory(bookDetails.getCategory());
        existedBook.setPublisher(bookDetails.getPublisher());

        return booksRepository.save(existedBook);
    }

    public void deleteBook (String id){
        booksRepository.deleteById(id);
    }

    public Page<Book> getAllBooksPaging(int page, int size, String sortBy){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return booksRepository.findAll(pageable);
    }
}
