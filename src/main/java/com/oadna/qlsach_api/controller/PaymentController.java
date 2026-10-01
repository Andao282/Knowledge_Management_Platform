package com.oadna.qlsach_api.controller;

import com.oadna.qlsach_api.config.VNPayConfig;
import com.oadna.qlsach_api.entity.FineSlips;
import com.oadna.qlsach_api.repository.FineSlipsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private FineSlipsRepository  fineSlipsRepository;

    // Kéo các cấu hình từ application.properties lên đây
    @Value("${vnpay.tmnCode}")
    private String vnp_TmnCode;
    @Value("${vnpay.hashSecret}")
    private String vnp_HashSecret;
    @Value("${vnpay.url}")
    private String vnp_PayUrl;
    @Value("${vnpay.returnUrl}")
    private String vnp_ReturnUrl;

    // API này dùng để tạo URL chuyển hướng sang trang VNPay
    @GetMapping("/create-payment")
    public String createPayment(@RequestParam String fineId, @RequestParam long amount) throws Exception {

        // VNPay quy định: Số tiền phải nhân với 100 (VD: 50.000 VNĐ -> gửi sang là 5000000)
        long amountVNPay = amount * 100;

        // 1. Khai báo các thông số bắt buộc
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amountVNPay));
        vnp_Params.put("vnp_CurrCode", "VND");

        // Mã giao dịch (Phải là duy nhất nên anh nối thêm timestamp hiện tại)
        vnp_Params.put("vnp_TxnRef", fineId + "_" + System.currentTimeMillis());
        vnp_Params.put("vnp_OrderInfo", "Thanh toan tien phat cho phieu: " + fineId);
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", "127.0.0.1");

        // 2. Định dạng ngày giờ tạo giao dịch
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        vnp_Params.put("vnp_CreateDate", formatter.format(cld.getTime()));

        // 3. Xếp tham số theo thứ tự bảng chữ cái (VNPay bắt buộc làm vậy)
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);

        // 4. Tạo chuỗi URL và chuỗi Bảo mật (Hash)
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // Ép kiểu chuẩn web cho các chuỗi có khoảng trắng
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString())).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        // 5. Khóa dữ liệu bằng Hash Secret và hoàn thiện URL
        String vnp_SecureHash = VNPayConfig.hmacSHA512(vnp_HashSecret, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);

        return vnp_PayUrl + "?" + query.toString();
    }

    @GetMapping("/vnpay-callback")
    public String paymentCallback(@RequestParam Map<String, String> queryParams){
        String vnp_SecureHash = queryParams.get("vnp_SecureHash");

        queryParams.remove("vnp_SecureHashType");
        queryParams.remove("vnp_SecureHash");

        List<String> fieldNames = new ArrayList<>(queryParams.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();

        try{
            while(itr.hasNext()){
                String fieldName = itr.next();
                String fieldValue = queryParams.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()).replace("+", "%20"));
                    if(itr.hasNext()){
                        hashData.append('&');
                    }
                }
            }
        }catch(Exception e){
            return "Lỗi mã hóa dữ liệu!";
        }

        String signValue = VNPayConfig.hmacSHA512(vnp_HashSecret, hashData.toString());
        if(signValue.equals(vnp_SecureHash)){
            String responseCode =  queryParams.get("responseCode");
            if ("00".equals(responseCode)){
                String txnRef = queryParams.get("vnp_TxnRef");
                String fineId = txnRef.split("_")[0];

                FineSlips slip = fineSlipsRepository.findById(fineId).orElse(null);

                if(slip != null){
                    slip.setIsPaid(true);
                    fineSlipsRepository.save(slip);
                    return "Giao dịch THÀNH CÔNG! Đã gạch nợ cho phiếu phạt: " + fineId;
                }else {
                    return "Giao dịch thành công nhưng không tìm thấy phiếu phạt trong Database!";
                }
            }else{
                return "Giao dịch THẤT BẠI! Mã lỗi VNPay: " + responseCode;
            }
        }else{
            return "CẢNH BÁO: Chữ ký không hợp lệ! Phát hiện nghi vấn giả mạo dữ liệu.";
        }
    }
}