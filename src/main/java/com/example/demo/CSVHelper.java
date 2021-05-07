package com.example.demo;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;

@Service
public class CSVHelper {
    public static String TYPE = "text/csv";
    static String[] HEADERs = {"Adress", "Description", "year" };
    @Autowired
    public TagRepository tagRepository;
    @Autowired
    public AddressRepository addressRepository;
    @Autowired
    public BildRepository bildRepository;
    MainController mainController;

    public CSVHelper(MainController mainController){
        this.mainController = mainController;
    }


    public static boolean hasCSVFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }


    public String readFirstEntry (String entry){
        String[] arr = entry.trim().split("\\s\\s+");
        return arr[0];
    }

    private boolean avoidDuplicates(String documentID){
        Optional<Bilder> newBild = bildRepository.findBilderByDocumentIDEquals(documentID);
        if(newBild.isPresent()){
            return false;
        }
        return true;
    }

    public List<Bilder> csvToDatabase(InputStream input) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.EXCEL.withDelimiter(';').withFirstRecordAsHeader())) {

            List<Bilder> bildSamling = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {

                // Kolla om street och address är null och om dokumentid redan finns if, hoppa över:
                if ((!csvRecord.get(8).equals("") || !csvRecord.get(9).equals("")) && avoidDuplicates(csvRecord.get(13))){

                    ByteArrayOutputStream byteArray = new ByteArrayOutputStream();

                    Bilder b = new Bilder();

                    b.setDescription(csvRecord.get(2));
                    b.setYear(Integer.parseInt((csvRecord.get(4))));
                    b.setPhotographer(csvRecord.get(5));
                    b.setLicence(csvRecord.get(6));
                    b.setDocumentID(csvRecord.get(13));

                    if (!csvRecord.get(10).equals("")){
                        b.setBlock(readFirstEntry(csvRecord.get(10)));
                    }else{
                        b.setBlock("");
                    }

                    if (!csvRecord.get(11).equals("")){
                        b.setDistrict(readFirstEntry(csvRecord.get(11)));
                    }else{
                        b.setDistrict("");
                    }

                    if (!csvRecord.get(8).equals("")){
                        b.setAddresses(mainController.makeAddresses(csvRecord.get(8)));
                    }else {
                        b.setAddresses(mainController.makeAddresses(csvRecord.get(9)));
                    }

                    b.setTags(mainController.makeTags(csvRecord.get(7)));

                    String imagePath = "https://digitalastadsmuseet.stockholm.se";
                    imagePath += csvRecord.get(12);
                    URL imageUrl = new URL(imagePath);

                    // Läs in foto:
                    BufferedImage fetchedImage = ImageIO.read(imageUrl);

                    // Gör om till byte array:
                    ImageIO.write(fetchedImage, "jpg", byteArray);
                    byteArray.flush();
                    byte[] byteImage = byteArray.toByteArray();
                    byteArray.close();

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
