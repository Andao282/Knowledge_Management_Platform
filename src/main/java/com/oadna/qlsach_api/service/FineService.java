package com.oadna.qlsach_api.service;

import com.oadna.qlsach_api.entity.FineSlips;
import com.oadna.qlsach_api.repository.FineSlipsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FineService {
    @Autowired private FineSlipsRepository fineSlipsRepository;

    public String payFine(String fineID){
        FineSlips fineSlips = fineSlipsRepository.findById(fineID)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu phạt có mã " + fineID));

        if(fineSlips.getIsPaid() != null && fineSlips.getIsPaid()){
            return "Phiếu phạt này đã được thanh toán từ trước!";
        }

        fineSlips.setIsPaid(true);
        fineSlipsRepository.save(fineSlips);

        return "Đã thanh toán thành công cho phiếu phạt " + fineID;
    }
}
