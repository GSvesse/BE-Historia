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
    CSVHelper CH;

    public void save(MultipartFile file, MainController mainController) {
        CSVHelper CH = new CSVHelper(mainController);

        try {
            InputStream input = file.getInputStream();
            List<Bilder> bilder = CH.csvToDatabase(input) ;
            bildRepository.saveAll(bilder);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public List<Bilder> getAllBilder() {
        return (List<Bilder>) bildRepository.findAll();
    }
}