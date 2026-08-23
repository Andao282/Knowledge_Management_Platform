package com.oadna.qlsach_api.service;


import com.oadna.qlsach_api.dto.AuthorResponseDTO;
import com.oadna.qlsach_api.entity.Author;
import com.oadna.qlsach_api.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public List<Author> getAllAuthors(){
        return authorRepository.findAll();
    }

    public List<Author> createAuthor(List<Author> authors){
       for (Author author : authors) {
           if (authorRepository.existsById(author.getAuthorID())) {
               throw new RuntimeException("Mã tác giả đã tồn tại!");
           }
       }
       return authorRepository.saveAll(authors);

    }

    public Author getAuthorById(String id){
        return authorRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Mã tác giả không tồn tại!"));
    }

    public Author updateAuthor(String id, Author author){
        Author existing = getAuthorById(id);

        existing.setAuthorName(author.getAuthorName());
        existing.setAuthorHometown(author.getAuthorHometown());
        existing.setDob(author.getDob());
        existing.setPenName(author.getPenName());

        return authorRepository.save(existing);
    }

    public void deleteAuthor(String id){
        Author existing = getAuthorById(id);
        authorRepository.delete(existing);
    }

    public List<AuthorResponseDTO> getAuthorsByName(String name){
        List<Author> authors = authorRepository.findAuthorByNameIgnoreCase(name);

        return authors.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private AuthorResponseDTO mapToDTO(Author author){
        return AuthorResponseDTO.builder()
                .authorID(author.getAuthorID())
                .authorName(author.getAuthorName())
                .dob(author.getDob())
                .authorHometown(author.getAuthorHometown())
                .penName(author.getPenName())
                .build();
    }

}
