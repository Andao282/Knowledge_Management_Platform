package com.oadna.qlsach_api.service;

import com.oadna.qlsach_api.dto.ReaderRequestDTO;
import com.oadna.qlsach_api.dto.ReaderResponseDTO;
import com.oadna.qlsach_api.entity.Book;
import com.oadna.qlsach_api.entity.Reader;
import com.oadna.qlsach_api.repository.ReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<ReaderResponseDTO> createReaders(List<ReaderRequestDTO> request){
        return request.stream()
                .map(this::createReader)
                .collect(Collectors.toList());
    }

    public void deleteReader(String id){
        if(!readerRepository.existsById(id)){
            throw new RuntimeException("Không tìm thấy độc giả có mã: " + id);
        }
        readerRepository.deleteById(id);
    }

    public ReaderResponseDTO updateReader(String id, ReaderRequestDTO reader){
        Reader existedReader = readerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reader with id " + id + " not found"));

        //Lay du lieu moi (bookDetails) gan len du lieu cu
        existedReader.setReaderName(reader.getReaderName());
        existedReader.setAddress(reader.getReaderAddress());
        existedReader.setDob(reader.getDob());
        existedReader.setCardIssueDate(java.time.LocalDate.now());
        existedReader.setCardExpiryDate(java.time.LocalDate.now().plusYears(1));
        existedReader.setIsPaid(true);
        existedReader.setEmail(reader.getEmail());
        existedReader.setPhone(reader.getPhone());

        Reader updatedReader = readerRepository.save(existedReader);

        return mapToDTO(readerRepository.save(updatedReader));
    }

    public ReaderResponseDTO getReader(String id){
        Reader reader = readerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reader with id " + id + " not found"));
        return mapToDTO(reader);
    }

    public List<ReaderResponseDTO> getReadersByName(String name){
        List<Reader> readers = readerRepository.findReaderByReaderNameIgnoreCase(name);

        return readers.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
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
