package com.oadna.qlsach_api.repository;

import com.oadna.qlsach_api.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BooksRepository extends JpaRepository<Book, String> {

}
