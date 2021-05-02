package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//hejhej

@Controller
@CrossOrigin
@RequestMapping(path="/demo")
public class MainController {
    @Autowired
    private BildRepository bildRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    CSVService fileService;

    @PostMapping("/uploadcsv")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message;

        if (CSVHelper.hasCSVFormat(file)) {
            try {
                fileService.save(file);

                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        }

        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }


    @PostMapping(path="bilder/add")
    public @ResponseBody String addNewBild(@RequestParam("image")MultipartFile file, @RequestParam int year, @RequestParam String addresses, @RequestParam String tags, @RequestParam String documentID, @RequestParam String photographer, @RequestParam String licence, @RequestParam String block, @RequestParam String district, @RequestParam String description) throws IOException {
        Bilder b = new Bilder();
        b.setImage(file.getBytes());
        b.setYear(year);
        b.setAddresses(makeAddresses(addresses));
        b.setTags(makeTags(tags));
        b.setDocumentID(documentID);
        b.setPhotographer(photographer);
        b.setLicence(licence);
        b.setBlock(block);
        b.setDistrict(district);
        b.setDescription(description);
        bildRepository.save(b);
        return "Saved";
    }

    @PostMapping(path = "tag/add")
    public @ResponseBody String addNewTag(@RequestParam String tag){
        Tag t = new Tag();
        t.setTag(tag);
        tagRepository.save(t);
        return "Saved";
    }

    @PostMapping(path = "address/add")
    public @ResponseBody String addNewAddress(@RequestParam String address){
        Address a = new Address();
        a.setAddress(address);
        addressRepository.save(a);
        return "Saved";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Bilder>getAllBilder(){
        return bildRepository.findAll();
    }
    @GetMapping(path="/addresses")
    public @ResponseBody Iterable<Address>getAllAddresses(){
        return addressRepository.findAll();
    }

    @GetMapping("files/{id}")
    public ResponseEntity<byte[]> fromDatabaseAsResEntity(@PathVariable("id") Integer id) throws SQLException {

        Optional<Bilder> bild = bildRepository.findById(id);
        byte[] imageBytes = null;
        if (bild.isPresent()) {

            imageBytes = bild.get().getImage();
        }

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
    }

    /** Gör om en sträng med taggar till List med Tag-objekt
     * Skapar ny Tag om taggen inte finns. returnerar sedan listan
     * @Param tagString sträng med taggar separerade med mer än ett space*/
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
    /** Gör om en sträng med adresser till List med Address-objekt
     * Skapar ny Address om adressen inte finns. returnerar sedan listan.
     * @Param addressString sträng med adresser separerade med mer än ett space*/
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
    }

}
