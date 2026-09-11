package com.oadna.qlsach_api.scheduler;

import com.oadna.qlsach_api.entity.BorrowDetails;
import com.oadna.qlsach_api.entity.FineSlips;
import com.oadna.qlsach_api.repository.BorrowDetailsRepository;
import com.oadna.qlsach_api.repository.FineSlipsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
public class LibraryScheduler {
    @Autowired
    private BorrowDetailsRepository borrowDetailsRepository;

    @Autowired
    private FineSlipsRepository fineSlipsRepository;

    //@Scheduled(cron = "0 0 0 * * ?")
    @Scheduled(fixedRate = 10000)
    @Transactional
    public void scanAndPunishOverdueBorrowers(){
        System.out.println("Bắt đầu quét hệ thống...");

        LocalDate today = LocalDate.now();
        List<BorrowDetails> overdueList = borrowDetailsRepository.findByActualReturnDateIsNullAndExpectedReturnDateBefore(today);

        if(overdueList.isEmpty()){
            System.out.println("Không có ai trễ hạn hôm nay!");
            return;
        }

        for(BorrowDetails borrowDetails : overdueList){
            long daysLate = java.time.temporal.ChronoUnit.DAYS.between(borrowDetails.getExpectedReturnDate(), LocalDate.now());

            double fineAmount = daysLate * 5000;
            String fineReason = "Trễ hạn " + daysLate + " ngày!";

            java.util.Optional<FineSlips> existingFineSlip = fineSlipsRepository.findByBorrowDetailsAndIsPaidFalse(borrowDetails);

            if(existingFineSlip.isPresent()){
                FineSlips existingSlip = existingFineSlip.get();
                existingSlip.setFineAmount(fineAmount);
                existingSlip.setFineReason(fineReason);
                existingSlip.setFineDate(today);

                fineSlipsRepository.save(existingSlip);

                System.out.println("Đã cập nhật phiếu phạt: " + existingSlip.getFineID()
                                    + "Tổng tiền: " + fineAmount + " VND");
            }
            else{

            FineSlips fineSlips = new FineSlips();
            fineSlips.setFineID("PP-" + UUID.randomUUID().toString().substring(0, 4));
            fineSlips.setBorrowDetails(borrowDetails);
            fineSlips.setFineDate(today);
            fineSlips.setFineReason(fineReason);
            fineSlips.setFineAmount(fineAmount);
            fineSlips.setIsPaid(false);
            fineSlipsRepository.save(fineSlips);

            System.out.println("Mã phiếu: " + borrowDetails.getId().getBorrowID()
                                + "Mã sách: " + borrowDetails.getId().getBookID()
                                + " đã trễ hạn: " + daysLate + " ngày!");
            }
        }
    }
}
