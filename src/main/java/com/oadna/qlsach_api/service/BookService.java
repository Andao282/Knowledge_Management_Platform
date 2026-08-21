package com.oadna.qlsach_api.service;

import java.util.stream.Collectors;

import com.oadna.qlsach_api.dto.BookRequestDTO;
import com.oadna.qlsach_api.dto.BookResponseDTO;
import com.oadna.qlsach_api.entity.Author;
import com.oadna.qlsach_api.entity.Book;
import com.oadna.qlsach_api.entity.Category;
import com.oadna.qlsach_api.entity.Publisher;
import com.oadna.qlsach_api.repository.AuthorRepository;
import com.oadna.qlsach_api.repository.BooksRepository;
import com.oadna.qlsach_api.repository.CategoryRepository;
import com.oadna.qlsach_api.repository.PublisherRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private BooksRepository booksRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    //Lay API toan bo sach
    public List<BookResponseDTO> getAllBooks() {
        List<Book> books = booksRepository.findAll();
        return books.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    //API them sach
    public BookResponseDTO addBook(BookRequestDTO bookRequestDTO) {
        if(!booksRepository.existsById(bookRequestDTO.getBookID())){
            throw new RuntimeException("Mã sách đã tồn tại!");
        }

        Author author = authorRepository.findById(bookRequestDTO.getAuthorID())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tác giả mã: "+bookRequestDTO.getAuthorID()));

        Publisher publisher = publisherRepository.findById(bookRequestDTO.getPublisherID())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy NXB có mã: " +bookRequestDTO.getPublisherID()));

        Category category = categoryRepository.findById(bookRequestDTO.getCategoryID())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thể loại có mã: " + bookRequestDTO.getCategoryID()));

        Book newBook = new Book();
        newBook.setBookID(bookRequestDTO.getBookID());
        newBook.setBookName(bookRequestDTO.getBookName());
        newBook.setAuthor(author);
        newBook.setPublisher(publisher);
        newBook.setCategory(category);

        Book savedBook = booksRepository.save(newBook);

        return mapToDTO(savedBook);
    }

    public List<BookResponseDTO> addBooks(List<BookRequestDTO> bookRequest) {
        return bookRequest.stream()
                .map(this ::addBook)
                .collect(Collectors.toList());
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
        if (!booksRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy sách có mã: " + id);
        }
        booksRepository.deleteById(id);
    }

    public BookResponseDTO getBookById(String id){
        Book book = booksRepository.findById(id).
              orElseThrow(() -> new RuntimeException("Không tìm thấy sách có mã: " + id));

        return mapToDTO(book);
    }

    public List<BookResponseDTO> findBookByBookNameContainingIgnoreCase(String bookName){
        List<Book> books = booksRepository.findBookByBookNameContainingIgnoreCase(bookName);

        return books.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<BookResponseDTO> findBookByCategory_CategoryNameIgnoreCase(String categoryName){
        List<Book> books = booksRepository.findByCategory_CategoryNameIgnoreCase(categoryName);

        return books.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Page<BookResponseDTO> getAllBooksPaging(int page, int size, String sortBy){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        Page<Book> bookPage = booksRepository.findAll(pageable);
        return bookPage.map(this::mapToDTO);
    }

    private BookResponseDTO mapToDTO(Book book){
        return BookResponseDTO.builder()
                .bookID(book.getBookID())
                .bookName(book.getBookName())
                .authorName(book.getAuthor().getAuthorName())
                .categoryName(book.getCategory().getCategoryName())
                .publisherName(book.getPublisher().getPublisherName())
                .build();
    }
}
