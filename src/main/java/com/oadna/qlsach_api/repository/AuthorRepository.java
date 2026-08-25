package com.oadna.qlsach_api.repository;

import com.oadna.qlsach_api.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author,String> {
    List<Author> findAuthorByAuthorNameIgnoreCase(String name);
}
