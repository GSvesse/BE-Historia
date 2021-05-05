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
    MainController mainController;

    public CSVHelper(MainController mainController){
        this.mainController = mainController;
    }


    public static boolean hasCSVFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

/*    *//** Gör om en sträng med taggar till List med Tag-objekt
     * Skapar ny Tag om taggen inte finns. returnerar sedan listan
     * @Param tagString sträng med taggar separerade med mer än ett space*//*
    public List<Tag> makeTags(String tagString){
        //tar in en sträng med taggar separerade med mer än ett space och delar på dem, lägger dem i en lista
        String[] tagArr = tagString.trim().split("\\s\\s+");
        List<Tag> result = new ArrayList<>();

        //går igenom listan och ser om taggen redan finns i tagRepository. Finns den inte skapas ny tag
        for (String newTag : tagArr){
            if (tagRepository.findByTag(newTag).isEmpty()) {
                Tag tag = new Tag();
                tag.setTag(newTag);
                System.out.println(tag);
                tagRepository.save(tag);
            }
            //Söker genom tagRepository efter ett entry med taggen newTag och lägger till i resultat-lista
            result.add(tagRepository.findByTag(newTag).get(0));
        }
        return result;
    }
    *//** Gör om en sträng med adresser till List med Address-objekt
     * Skapar ny Address om adressen inte finns. returnerar sedan listan.
     * @Param addressString sträng med adresser separerade med mer än ett space*//*
    public List<Address> makeAddresses (String addressString){
        String[] addressArr = addressString.trim().split("\\s\\s+");
        List<Address> result = new ArrayList<>();
        for (String newAddress : addressArr){
            if (addressRepository.findByAddress(newAddress).isEmpty()){
                Address address = new Address();
                address.setAddress(newAddress);
                addressRepository.save(address);
            }
            result.add(addressRepository.findByAddress(newAddress).get(0));
        }
        return result;
    }*/

    public List<Bilder> csvToDatabase(InputStream input) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.EXCEL.withDelimiter(';').withFirstRecordAsHeader())) {

            List<Bilder> bildSamling = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();

            for (CSVRecord csvRecord : csvRecords) {

                // Kolla om street och address är null if, hoppa över:
                if (!csvRecord.get("address").equals("") && !csvRecord.get("street").equals("")){

                    Bilder b = new Bilder();

                    b.setDescription(csvRecord.get(2));
                    b.setYear(Integer.parseInt((csvRecord.get(4))));
                    b.setPhotographer(csvRecord.get(5));
                    b.setLicence(csvRecord.get(6));
                    b.setTags(mainController.makeTags(csvRecord.get(7)));
                    b.setBlock(csvRecord.get(10));
                    b.setDistrict(csvRecord.get(11));

                    if (!csvRecord.get(8).equals("")){
                        b.setAddresses(mainController.makeAddresses(csvRecord.get(8)));
                    } else {
                        b.setAddresses(mainController.makeAddresses(csvRecord.get(9)));
                    }

                    String imagePath = "https://digitalastadsmuseet.stockholm.se";
                    imagePath += csvRecord.get(12);
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
