package com.example.demo;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;

public class CSVHelper {
    public static String TYPE = "text/csv";
    //static String[] HEADERs = { lägg eventuellt till headers "såhär" };

    public static boolean hasCSVFormat(MultipartFile file) {

        return TYPE.equals(file.getContentType());
    }

    public static List<Bilder> csvToDatabase(InputStream input) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<Bilder> bildSamling = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                // Kolla om street och address är null if, hoppa över.
                Bilder b = new Bilder();
                // Kolla om både gata och adress är NULL
                b.setDescription(csvRecord.get("Description"));
                b.setYear(Integer.parseInt((csvRecord.get("year"))));
                b.setPhotographer(csvRecord.get("Photographer"));
                b.setLicence(csvRecord.get("licence"));
                // hoppar över tag, street och address, skicka som sträng
                // Skicka tag som sträng
                b.setBlock(csvRecord.get("block"));
                b.setDistrict(csvRecord.get("district"));

                String urlToPhoto = "https://digitalastadsmuseet.stockholm.se";
                urlToPhoto += csvRecord.get("Photo-src");
                // Hämta som multipart file
                // skriv den som getbytes.
                BufferedImage myPicture = ImageIO.read(new File(urlToPhoto));


                bildSamling.add(b);
            }

            return bildSamling;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

}
