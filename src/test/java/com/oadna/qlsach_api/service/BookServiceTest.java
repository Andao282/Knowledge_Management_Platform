package com.oadna.qlsach_api.service;

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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    private static final String BOOK_ID = "B01";
    private static final String AUTHOR_ID = "A01";
    private static final String CATEGORY_ID = "C01";
    private static final String PUBLISHER_ID = "P01";

    @Mock
    private BooksRepository booksRepository;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private PublisherRepository publisherRepository;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void addBook_mapsRelationsAndReturnsResponse() {
        when(booksRepository.existsById(BOOK_ID)).thenReturn(false);
        when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.of(author()));
        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(category()));
        when(publisherRepository.findById(PUBLISHER_ID)).thenReturn(Optional.of(publisher()));
        when(booksRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookResponseDTO response = bookService.addBook(bookRequest());

        assertThat(response.getBookID()).isEqualTo(BOOK_ID);
        assertThat(response.getBookName()).isEqualTo("Clean Code");
        assertThat(response.getAuthorName()).isEqualTo("Robert C. Martin");
        assertThat(response.getCategoryName()).isEqualTo("Software Engineering");
        assertThat(response.getPublisherName()).isEqualTo("Prentice Hall");
        assertThat(response.getQuantity()).isEqualTo(5);
    }

    @Test
    void addBook_throwsWhenBookIdAlreadyExists() {
        when(booksRepository.existsById(BOOK_ID)).thenReturn(true);

        assertThatThrownBy(() -> bookService.addBook(bookRequest()))
                .isInstanceOf(RuntimeException.class);

        verify(authorRepository, never()).findById(any());
        verify(booksRepository, never()).save(any());
    }

    @Test
    void addBook_throwsWhenAuthorDoesNotExist() {
        when(booksRepository.existsById(BOOK_ID)).thenReturn(false);
        when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.addBook(bookRequest()))
                .isInstanceOf(RuntimeException.class);

        verify(booksRepository, never()).save(any());
    }

    @Test
    void getBookById_throwsWhenBookDoesNotExist() {
        when(booksRepository.findById("B99")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.getBookById("B99"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void deleteBook_throwsWhenBookDoesNotExist() {
        when(booksRepository.existsById("B99")).thenReturn(false);

        assertThatThrownBy(() -> bookService.deleteBook("B99"))
                .isInstanceOf(RuntimeException.class);

        verify(booksRepository, never()).deleteById(any());
    }

    private BookRequestDTO bookRequest() {
        return BookRequestDTO.builder()
                .bookID(BOOK_ID)
                .bookName("Clean Code")
                .authorID(AUTHOR_ID)
                .categoryID(CATEGORY_ID)
                .publisherID(PUBLISHER_ID)
                .quantity(5)
                .build();
    }

    private Author author() {
        Author author = new Author();
        author.setAuthorID(AUTHOR_ID);
        author.setAuthorName("Robert C. Martin");
        return author;
    }

    private Category category() {
        Category category = new Category();
        category.setCategoryID(CATEGORY_ID);
        category.setCategoryName("Software Engineering");
        return category;
    }

    private Publisher publisher() {
        Publisher publisher = new Publisher();
        publisher.setPublisherID(PUBLISHER_ID);
        publisher.setPublisherName("Prentice Hall");
        return publisher;
    }
}
