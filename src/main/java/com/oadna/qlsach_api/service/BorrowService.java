package com.oadna.qlsach_api.service;

import com.oadna.qlsach_api.dto.BookBorrowDetailRequestDTO;
import com.oadna.qlsach_api.dto.BorrowRequestDTO;
import com.oadna.qlsach_api.entity.*;
import com.oadna.qlsach_api.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class BorrowService {
    @Autowired private BooksRepository booksRepository;
    @Autowired private StaffRepository staffRepository;
    @Autowired private ReaderRepository readerRepository;
    @Autowired private BorrowSlipsRepository borrowSlipsRepository;
    @Autowired private BorrowDetailsRepository borrowDetailsRepository;

    @Transactional
    public String createBorrowSlip(BorrowRequestDTO requestDTO){
        Reader reader = readerRepository.findById(requestDTO.getReaderID())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy độc giả có id: " + requestDTO.getReaderID()));

        Staff staff = staffRepository.findById(requestDTO.getStaffID())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên có mã: " + requestDTO.getStaffID()));

        BorrowSlips borrowSlips = new BorrowSlips();
        //Tự động sinh mã phiếu mượn (4 chữ/số)
        String newBorrowID = "PM-" + UUID.randomUUID().toString().substring(0, 4);

        borrowSlips.setBorrowID(newBorrowID);
        borrowSlips.setReaderID(reader);
        borrowSlips.setStaffID(staff);
        borrowSlips.setBorrowDate(LocalDate.now());
        borrowSlips.setDueDate(LocalDate.now().plusDays(30));//Mặc định cho mượn 30 ngày

        borrowSlipsRepository.save(borrowSlips);

        for (BookBorrowDetailRequestDTO detailDTO : requestDTO.getDetails()){
            Book book = booksRepository.findById(detailDTO.getBookID())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sách có mã: " + detailDTO.getBookID()));

            if(book.getQuantity() == null || book.getQuantity() <= 0){
                throw new RuntimeException("Sách "+ book.getBookName() + " đã hết trong kho!");
            }

            book.setQuantity(book.getQuantity() - 1);
            booksRepository.save(book);

            BorrowDetailsID detailsID = new BorrowDetailsID();
            detailsID.setBorrowID(newBorrowID);
            detailsID.setBookID(book.getBookID());

            BorrowDetails details = new BorrowDetails();
            details.setId(detailsID);
            details.setBorrowSlips(borrowSlips);
            details.setBook(book);
            details.setBorrowCondition("Bình thường");
            details.setExpectedReturnDate(requestDTO.getDueDate());
            details.setActualReturnDate(null);
            details.setReturnCondition(null);

            borrowDetailsRepository.save(details);
        }
        return "Lập phiếu mượn thành công! Mã phiếu: " + newBorrowID;

    }
}
