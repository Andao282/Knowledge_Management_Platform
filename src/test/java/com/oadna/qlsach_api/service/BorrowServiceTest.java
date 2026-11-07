package com.oadna.qlsach_api.service;

import com.oadna.qlsach_api.dto.BookBorrowDetailRequestDTO;
import com.oadna.qlsach_api.dto.BookReturnDetailRequestDTO;
import com.oadna.qlsach_api.dto.BorrowRequestDTO;
import com.oadna.qlsach_api.dto.ReturnRequestDTO;
import com.oadna.qlsach_api.entity.Book;
import com.oadna.qlsach_api.entity.BorrowDetails;
import com.oadna.qlsach_api.entity.BorrowDetailsID;
import com.oadna.qlsach_api.entity.BorrowSlips;
import com.oadna.qlsach_api.entity.FineSlips;
import com.oadna.qlsach_api.entity.Reader;
import com.oadna.qlsach_api.entity.Staff;
import com.oadna.qlsach_api.repository.BooksRepository;
import com.oadna.qlsach_api.repository.BorrowDetailsRepository;
import com.oadna.qlsach_api.repository.BorrowSlipsRepository;
import com.oadna.qlsach_api.repository.FineSlipsRepository;
import com.oadna.qlsach_api.repository.ReaderRepository;
import com.oadna.qlsach_api.repository.StaffRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BorrowServiceTest {

    private static final String READER_ID = "R01";
    private static final String STAFF_ID = "S01";
    private static final String BOOK_ID = "B01";
    private static final String BORROW_ID = "PM-0001";

    @Mock
    private BooksRepository booksRepository;
    @Mock
    private StaffRepository staffRepository;
    @Mock
    private ReaderRepository readerRepository;
    @Mock
    private BorrowSlipsRepository borrowSlipsRepository;
    @Mock
    private BorrowDetailsRepository borrowDetailsRepository;
    @Mock
    private FineSlipsRepository fineSlipsRepository;

    @InjectMocks
    private BorrowService borrowService;

    @Test
    void createBorrowSlip_decrementsStockAndPersistsDetails() {
        Reader reader = reader();
        Staff staff = staff();
        Book book = book(3);
        when(readerRepository.findById(READER_ID)).thenReturn(Optional.of(reader));
        when(staffRepository.findById(STAFF_ID)).thenReturn(Optional.of(staff));
        when(booksRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));

        String result = borrowService.createBorrowSlip(borrowRequest());

        assertThat(result).contains("PM-");
        assertThat(book.getQuantity()).isEqualTo(2);
        verify(booksRepository).save(book);

        ArgumentCaptor<BorrowDetails> captor = ArgumentCaptor.forClass(BorrowDetails.class);
        verify(borrowDetailsRepository).save(captor.capture());
        BorrowDetails saved = captor.getValue();
        assertThat(saved.getBorrowSlips().getReaderID()).isEqualTo(reader);
        assertThat(saved.getBorrowSlips().getStaffID()).isEqualTo(staff);
        assertThat(saved.getBook()).isEqualTo(book);
        assertThat(saved.getExpectedReturnDate()).isEqualTo(LocalDate.now().plusDays(30));
        assertThat(saved.getId().getBorrowID()).isEqualTo(saved.getBorrowSlips().getBorrowID());
    }

    @Test
    void createBorrowSlip_throwsWhenBookIsOutOfStock() {
        when(readerRepository.findById(READER_ID)).thenReturn(Optional.of(reader()));
        when(staffRepository.findById(STAFF_ID)).thenReturn(Optional.of(staff()));
        when(booksRepository.findById(BOOK_ID)).thenReturn(Optional.of(book(0)));

        assertThatThrownBy(() -> borrowService.createBorrowSlip(borrowRequest()))
                .isInstanceOf(RuntimeException.class);

        verify(borrowDetailsRepository, never()).save(any());
    }

    @Test
    void createBorrowSlip_throwsWhenReaderDoesNotExist() {
        when(readerRepository.findById(READER_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> borrowService.createBorrowSlip(borrowRequest()))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void returnBooks_createsOverdueFineWhenReturnedLate() {
        BorrowSlips slip = slip(false);
        Book book = book(1);
        BorrowDetails detail = detail(slip, book, LocalDate.now().minusDays(5));
        stubReturn(slip, detail);

        borrowService.returnBooks(returnRequest("Bình thường"));

        ArgumentCaptor<FineSlips> captor = ArgumentCaptor.forClass(FineSlips.class);
        verify(fineSlipsRepository).save(captor.capture());
        FineSlips fine = captor.getValue();
        assertThat(fine.getFineAmount()).isEqualTo(25_000.0);
        assertThat(fine.getIsPaid()).isFalse();
        assertThat(fine.getFineReason()).contains("5");
        assertThat(detail.getActualReturnDate()).isEqualTo(LocalDate.now());
        assertThat(book.getQuantity()).isEqualTo(2);
        assertThat(slip.getReturned()).isTrue();
    }

    @Test
    void returnBooks_addsDamageFeeOnTopOfOverdueFine() {
        BorrowSlips slip = slip(false);
        Book book = book(1);
        BorrowDetails detail = detail(slip, book, LocalDate.now().minusDays(2));
        stubReturn(slip, detail);

        borrowService.returnBooks(returnRequest("Rách bìa"));

        ArgumentCaptor<FineSlips> captor = ArgumentCaptor.forClass(FineSlips.class);
        verify(fineSlipsRepository).save(captor.capture());
        assertThat(captor.getValue().getFineAmount()).isEqualTo(60_000.0);
    }

    @Test
    void returnBooks_doesNotCreateFineWhenReturnedOnTimeAndInGoodCondition() {
        BorrowSlips slip = slip(false);
        Book book = book(1);
        BorrowDetails detail = detail(slip, book, LocalDate.now().plusDays(3));
        stubReturn(slip, detail);

        borrowService.returnBooks(returnRequest("Bình thường"));

        verify(fineSlipsRepository, never()).save(any());
        assertThat(book.getQuantity()).isEqualTo(2);
        assertThat(slip.getReturned()).isTrue();
    }

    @Test
    void returnBooks_throwsWhenSlipWasAlreadyReturned() {
        BorrowSlips slip = slip(true);
        when(borrowSlipsRepository.findById(BORROW_ID)).thenReturn(Optional.of(slip));

        assertThatThrownBy(() -> borrowService.returnBooks(returnRequest("Bình thường")))
                .isInstanceOf(RuntimeException.class);

        verify(borrowDetailsRepository, never()).findById(any());
    }

    private void stubReturn(BorrowSlips slip, BorrowDetails detail) {
        when(borrowSlipsRepository.findById(BORROW_ID)).thenReturn(Optional.of(slip));
        when(borrowDetailsRepository.findById(any(BorrowDetailsID.class))).thenReturn(Optional.of(detail));
        when(borrowDetailsRepository.findById_BorrowID(BORROW_ID)).thenReturn(List.of(detail));
    }

    private Reader reader() {
        Reader reader = new Reader();
        reader.setReaderID(READER_ID);
        reader.setReaderName("Nguyen Van A");
        return reader;
    }

    private Staff staff() {
        Staff staff = new Staff();
        staff.setStaffID(STAFF_ID);
        staff.setStaffName("Tran Thi B");
        return staff;
    }

    private Book book(int quantity) {
        Book book = new Book();
        book.setBookID(BOOK_ID);
        book.setBookName("Clean Code");
        book.setQuantity(quantity);
        return book;
    }

    private BorrowSlips slip(boolean returned) {
        BorrowSlips slip = new BorrowSlips();
        slip.setBorrowID(BORROW_ID);
        slip.setBorrowDate(LocalDate.now().minusDays(10));
        slip.setDueDate(LocalDate.now().minusDays(1));
        slip.setReturned(returned);
        return slip;
    }

    private BorrowDetails detail(BorrowSlips slip, Book book, LocalDate expectedReturnDate) {
        BorrowDetailsID id = new BorrowDetailsID();
        id.setBorrowID(BORROW_ID);
        id.setBookID(BOOK_ID);

        BorrowDetails detail = new BorrowDetails();
        detail.setId(id);
        detail.setBorrowSlips(slip);
        detail.setBook(book);
        detail.setBorrowCondition("Bình thường");
        detail.setExpectedReturnDate(expectedReturnDate);
        return detail;
    }

    private BorrowRequestDTO borrowRequest() {
        BookBorrowDetailRequestDTO detail = new BookBorrowDetailRequestDTO();
        detail.setBookID(BOOK_ID);
        detail.setBorrowCondition("Bình thường");

        BorrowRequestDTO request = new BorrowRequestDTO();
        request.setReaderID(READER_ID);
        request.setStaffID(STAFF_ID);
        request.setDetails(List.of(detail));
        return request;
    }

    private ReturnRequestDTO returnRequest(String condition) {
        BookReturnDetailRequestDTO detail = new BookReturnDetailRequestDTO();
        detail.setBookID(BOOK_ID);
        detail.setReturnCondition(condition);

        ReturnRequestDTO request = new ReturnRequestDTO();
        request.setBorrowID(BORROW_ID);
        request.setDetails(List.of(detail));
        return request;
    }
}
