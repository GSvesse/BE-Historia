package com.example.demo;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
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
    //static String[] HEADERs = { lägg eventuellt till headers "såhär", "test" };

    public static boolean hasCSVFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public static List<Bilder> csvToDatabase(InputStream input) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<Bilder> bildSamling = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();

            for (CSVRecord csvRecord : csvRecords) {

                // Kolla om street och address är null if, hoppa över:
                if (!(csvRecord.get("Adress").equals("null") || csvRecord.get("street").equals("null"))){
                    Bilder b = new Bilder();

                    b.setDescription(csvRecord.get("Description"));
                    b.setYear(Integer.parseInt((csvRecord.get("year"))));
                    b.setPhotographer(csvRecord.get("Photographer"));
                    b.setLicence(csvRecord.get("license"));

                    if (!csvRecord.get("Adress").equals("null")){
                        b.setAddresses(csvRecord.get("Adress"));
                    } else {
                        b.setAddresses(csvRecord.get("street"));
                    }

                    b.setTags(csvRecord.get("Tag"));

                    b.setBlock(csvRecord.get("block"));
                    b.setDistrict(csvRecord.get("district"));

                    String imagePath = "https://digitalastadsmuseet.stockholm.se";
                    imagePath += csvRecord.get("Photo-src");
                    URL imageUrl = new URL(imagePath);

                    // Läs in foto:
                    BufferedImage fetchedImage = ImageIO.read(imageUrl);

                    // Gör om till byte array:
                    ImageIO.write(fetchedImage, "jpg", byteArray);
                    byteArray.flush();
                    byte[] byteImage = byteArray.toByteArray();

                    // Sätt till att vara bildens foto:
                    b.setImage(byteImage);



                    bildSamling.add(b);
                }
            }
            return bildSamling;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

}
