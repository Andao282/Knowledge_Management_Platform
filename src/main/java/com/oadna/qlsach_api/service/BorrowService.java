package com.oadna.qlsach_api.service;

import com.oadna.qlsach_api.dto.*;
import com.oadna.qlsach_api.entity.*;
import com.oadna.qlsach_api.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class BorrowService {
    @Autowired private BooksRepository booksRepository;
    @Autowired private StaffRepository staffRepository;
    @Autowired private ReaderRepository readerRepository;
    @Autowired private BorrowSlipsRepository borrowSlipsRepository;
    @Autowired private BorrowDetailsRepository borrowDetailsRepository;
    @Autowired private FineSlipsRepository fineSlipsRepository;

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
            details.setExpectedReturnDate(borrowSlips.getDueDate());
            details.setActualReturnDate(null);
            details.setReturnCondition(null);

            borrowDetailsRepository.save(details);
        }
        return "Lập phiếu mượn thành công! Mã phiếu: " + newBorrowID;

    }

    @Transactional
    public String returnBooks(ReturnRequestDTO requestDTO){
        BorrowSlips slip = borrowSlipsRepository.findById(requestDTO.getBorrowID())
                .orElseThrow(() -> new RuntimeException( "Không tìm thấy phiếu mượn có mã " + requestDTO.getBorrowID()));

        if(slip.getReturned() != null && slip.getReturned()){
            throw new RuntimeException("Phiếu mượn có mã " + requestDTO.getBorrowID() + "đã được trả trước đó!");
        }

        for (BookReturnDetailRequestDTO returnDetail : requestDTO.getDetails()){
            BorrowDetailsID detailsID = new BorrowDetailsID();
            detailsID.setBorrowID(requestDTO.getBorrowID());
            detailsID.setBookID(returnDetail.getBookID());

            BorrowDetails detail = borrowDetailsRepository.findById(detailsID)
                    .orElseThrow(() -> new RuntimeException("Sách có mã " + returnDetail.getBookID() + " không có trong phiếu mượn!"));

            if (detail.getActualReturnDate() != null){
                throw new RuntimeException("Sách có mã " + returnDetail.getBookID() + " đã được hoàn trả ngày " + detail.getActualReturnDate());
            }

            detail.setActualReturnDate(LocalDate.now());
            detail.setReturnCondition(returnDetail.getReturnCondition());
            borrowDetailsRepository.save(detail);

            double fineAmount = 0.0;
            StringBuilder fineReason = new StringBuilder();

            long daysLate = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), detail.getExpectedReturnDate());
            if(daysLate > 0){
                fineAmount += daysLate * 5000;
                fineReason.append("Trễ hạn ").append(daysLate).append(" ngày.");
            }

            if(!detail.getReturnCondition().equalsIgnoreCase("Bình thường")){
                fineAmount += 50000;
                fineReason.append("Tình trạng trả: ").append(returnDetail.getReturnCondition());
            }

            if(fineAmount > 0){
                FineSlips fineSlips = new FineSlips();
                fineSlips.setFineID("PP-" +UUID.randomUUID().toString().substring(0, 4));

                fineSlips.setBorrowDetails(detail);
                fineSlips.setFineDate(LocalDate.now());
                fineSlips.setFineReason(fineReason.toString());
                fineSlips.setFineAmount(fineAmount);
                fineSlips.setIsPaid(false);

                fineSlipsRepository.save(fineSlips);
            }

            Book book = detail.getBook();
            book.setQuantity(book.getQuantity() + 1);
            booksRepository.save(book);
        }

        List<BorrowDetails> allDetails = borrowDetailsRepository.findById_BorrowID(requestDTO.getBorrowID());
        boolean isAllReturned = true;

        for (BorrowDetails borrowDetails : allDetails) {
            if(borrowDetails.getActualReturnDate() == null){
                isAllReturned = false;
                break;
            }
        }

        if (isAllReturned){
            slip.setReturned(true);
            return "Phiếu mượn " + requestDTO.getBorrowID() +" đã hoàn trả thành công!";
        }
        return "Đã ghi nhận phiếu mượn " + requestDTO.getBorrowID() + " được hoàn trả 1 phần!";
    }

    public Page<BorrowResponseDTO> getAllBorrowSlips(int pageNo, int pageSize, String sortBy){
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());

       Page<BorrowSlips> pageSlips = borrowSlipsRepository.findAll(pageable);
        return pageSlips.map(slip -> {
            BorrowResponseDTO dto = new BorrowResponseDTO();
            dto.setBorrowID(slip.getBorrowID());
            dto.setStaffID(slip.getStaffID().getStaffID());
            dto.setReaderID(slip.getReaderID().getReaderID());
            dto.setBorrowDate(slip.getBorrowDate());
            dto.setDueDate(slip.getDueDate());
            dto.setReturned(slip.getReturned());
            return dto;
        });
    }

    public BorrowSlipDetailResponseDTO getBorrowSlipByID(String borrowID){
        BorrowSlips slips = borrowSlipsRepository.findById(borrowID)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu mượn có mã " + borrowID));

        List<BorrowDetails> detailsList = borrowDetailsRepository.findById_BorrowID(borrowID);

        List<BorrowDetailItemDTO> detailDTOs = detailsList.stream().map(detail -> {
            BorrowDetailItemDTO detailDTO = new BorrowDetailItemDTO();
            detailDTO.setBookID(detail.getBook().getBookID());
            detailDTO.setBookName(detail.getBook().getBookName());
            detailDTO.setBorrowCondition(detail.getBorrowCondition());
            detailDTO.setExpectedReturnDate(detail.getExpectedReturnDate());
            detailDTO.setActualReturnDate(detail.getActualReturnDate());
            detailDTO.setReturnCondition(detail.getReturnCondition());

            return detailDTO;
        }).toList();

        BorrowSlipDetailResponseDTO responseDTO = new BorrowSlipDetailResponseDTO();
        responseDTO.setBorrowID(slips.getBorrowID());
        responseDTO.setStaffID(slips.getStaffID().getStaffID());
        responseDTO.setReaderID(slips.getReaderID().getReaderID());
        responseDTO.setBorrowDate(slips.getBorrowDate());
        responseDTO.setDueDate(slips.getDueDate());
        responseDTO.setReturned(slips.getReturned());
        responseDTO.setDetails(detailDTOs);

        return responseDTO;
    }

    public List<BorrowResponseDTO> getBorrowHistoryByReader(String readerID) {
        readerRepository.findById(readerID)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy độc giả có mã" + readerID));

        List<BorrowSlips> listSlips = borrowSlipsRepository.findByReaderID_ReaderID(readerID);

            return listSlips.stream().map(slip -> {
                BorrowResponseDTO dto = new BorrowResponseDTO();
                dto.setBorrowID(slip.getBorrowID());
                dto.setStaffID(slip.getStaffID().getStaffID());
                dto.setReaderID(slip.getReaderID().getReaderID());
                dto.setBorrowDate(slip.getBorrowDate());
                dto.setDueDate(slip.getDueDate());
                dto.setReturned(slip.getReturned());
                return dto;
            }).toList();
        }
}
