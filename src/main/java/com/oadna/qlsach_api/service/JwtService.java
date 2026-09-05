package com.oadna.qlsach_api.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    // Chữ ký bí mật của riêng Thư viện (Bắt buộc dài trên 32 ký tự để thuật toán HS256 chấp nhận)
    private final String SECRET = "DayLaKhoaBiMatCuaDuAnQuanLyThuVienQlsach22026PhaiDaiHon32KyTu";

    // Hàm 1: Đúc thẻ JWT
    public String generateToken(String username, String role) {
        // Tạo một cái túi nhỏ để đựng thông tin phụ (ở đây là chức vụ)
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)         // Gắn thông tin phụ (role)
                .setSubject(username)      // Gắn tên chủ thẻ (username)
                .setIssuedAt(new Date(System.currentTimeMillis())) // Ngày cấp (ngay lúc này)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Hạn sử dụng: 24 tiếng (1000ms * 60s * 60m * 24h)
                .signWith(getSignKey(), SignatureAlgorithm.HS256) // Đóng dấu mộc đỏ
                .compact(); // Nén lại thành chuỗi JWT thành phẩm
    }

    // Hàm 2: Tạo chìa khóa mã hóa từ chuỗi SECRET
    private Key getSignKey() {
        byte[] keyBytes = SECRET.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

public String extractRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role",  String.class);
}
}