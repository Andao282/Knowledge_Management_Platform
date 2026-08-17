package com.oadna.qlsach_api.repository;

import com.oadna.qlsach_api.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, String> {
    List<Book> findByBookNameIgnoreCase(String bookName);

    List<Book> findByCategory_CategoryNameIgnoreCase(String categoryName);
}
