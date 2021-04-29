package com.example.demo;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CSVService {
    @Autowired
    BildRepository bildRepository;

    public void save(MultipartFile file) {
        try {
            List<Bilder> bilder = CSVHelper.csvToDatabase(file.getInputStream());
            bildRepository.saveAll(bilder);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public List<Bilder> getAllBilder() {
        return (List<Bilder>) bildRepository.findAll();
    }
}