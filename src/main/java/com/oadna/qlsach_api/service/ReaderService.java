package com.oadna.qlsach_api.service;

import com.oadna.qlsach_api.dto.ReaderRequestDTO;
import com.oadna.qlsach_api.dto.ReaderResponseDTO;
import com.oadna.qlsach_api.entity.Reader;
import com.oadna.qlsach_api.repository.ReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReaderService {
    @Autowired
    private ReaderRepository readerRepository;

    public List<ReaderResponseDTO> getAllReaders() {
        return readerRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }
    public ReaderResponseDTO createReader(ReaderRequestDTO request) {
        Reader reader = new Reader();
        reader.setReaderID(request.getReaderID());
        reader.setReaderName(request.getReaderName());
        reader.setAddress(request.getReaderAddress()); // Tùy tên biến bên request của em
        reader.setDob(request.getDob());
        reader.setEmail(request.getEmail());
        reader.setCardIssueDate(java.time.LocalDate.now());
        reader.setCardExpiryDate(java.time.LocalDate.now().plusYears(1));
        reader.setIsPaid(true);
        Reader savedReader = readerRepository.save(reader);
        return mapToDTO(savedReader);
    }

    private ReaderResponseDTO mapToDTO(Reader reader){
        return ReaderResponseDTO.builder()
                .readerID(reader.getReaderID())
                .readerName(reader.getReaderName())
                .readerAddress(reader.getAddress())
                .dob(reader.getDob())
                .isPaid(reader.getIsPaid())
                .cardIssueDate(reader.getCardIssueDate())
                .cardExpiryDate(reader.getCardExpiryDate())
                .email(reader.getEmail())
                .build();
    }
}
