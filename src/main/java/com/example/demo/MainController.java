package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

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
    private CSVService fileService;

    @PostMapping("/uploadcsv")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message;

        if (CSVHelper.hasCSVFormat(file)) {
            try {
                fileService.save(file, this);

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

    @GetMapping(path = "/files/getByAddress/{address}")
    public @ResponseBody Iterable<Bilder>getByAddress(@PathVariable ("address") String addressName){
        Address address = addressRepository.findAddressByAddress(addressName);
        return bildRepository.findAllByAddressesContaining(address);
    }

    // ny version av hämtning av adresser
    @GetMapping(path = "/files/getByAddressLike/{address}")
    public @ResponseBody Iterable<Bilder>getByAddressLike(@PathVariable ("address") String addressName){
        /*List<Address> addresses = addressRepository.includeAllAddressesOnStreet(addressName);
        if (addresses.isEmpty()){
            return null;
        }*/
        List<Address> addresses = addressRepository.findByAddressIsContaining(addressName);
        if (addresses.isEmpty()){
            return null;
        }
        List<Bilder> pictures = new LinkedList<>();
        for (Address a : addresses){
            for (Bilder b : bildRepository.findAllByAddressesContaining(a)){
                pictures.add(b);
            }
        }
        return pictures;
    }

    @GetMapping(path = "/files/getByTag/{tag}")
    public @ResponseBody Iterable<Bilder> getByTag(@PathVariable ("tag") String tagName){
        Tag tag = tagRepository.findTagByTag(tagName);
        return bildRepository.findAllByTagsEquals(tag);
    }

    @GetMapping(path = "/files/getByDistrict/{district}")
    public @ResponseBody Iterable<Bilder> getByDistrict(@PathVariable ("district") String district){
        return bildRepository.findAllByDistrictEquals(district);
    }

    @GetMapping(path = "/files/getByYear")
    public @ResponseBody Iterable<Bilder> getByYear(@RequestParam int start, @RequestParam int end){
        return bildRepository.findAllByYearBetween(start, end);
    }


    @GetMapping("files/{id}")
    public ResponseEntity<byte[]> fromDatabaseAsResEntity(@PathVariable ("id") int id) throws SQLException {

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
    public List<Address> makeAddresses (String addressString) throws JsonProcessingException {
        String[] addressArr = addressString.trim().split("\\s\\s+");
        List<Address> result = new ArrayList<>();
        for (String newAddress : addressArr){
            if (addressRepository.findByAddress(newAddress).isEmpty()){
                Address address = new Address();
                address.setAddress(newAddress);
                double[] latlong = getCoordinatesFromAddress(newAddress);
                address.setLatitude(latlong[0]);
                address.setLongitude(latlong[1]);
                addressRepository.save(address);
            }
            result.add(addressRepository.findByAddress(newAddress).get(0));
        }
        return result;
    }

    private double[] getCoordinatesFromAddress(String address) throws JsonProcessingException {
        double[] latLong = new double[2];
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        String geocodeResponse = restTemplate.getForObject(
                "https://maps.googleapis.com/maps/api/geocode/json?address={address}&key=AIzaSyCgp3abC2j7C5SaW65u3jLMxUE4BjisXDo",
                    String.class, address);
        assert geocodeResponse != null;
        JsonNode responseJsonNode = mapper.readTree(geocodeResponse);
        JsonNode results = responseJsonNode.get("results");
        if(results.isEmpty()){
            System.out.println(address);
            latLong[0] = 0;
            latLong[1] = 0;
            return latLong;
        }
        JsonNode geometry = results.findValue("geometry");
        JsonNode location = geometry.findValue("location");
        double lat = location.get("lat").asDouble();
        double lng = location.get("lng").asDouble();
        latLong[0] = lat;
        latLong[1] = lng;
        try {
            Thread.sleep( 1000);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        return latLong;
    }

    public boolean avoidDuplicates(String documentID){
        Optional<Bilder> newBild = bildRepository.findBilderByDocumentIDEquals(documentID);
        return !newBild.isPresent();
    }

    @GetMapping(path= "/setCoords")
    public void setAllAddressCoords() throws JsonProcessingException {
        for (Address address : addressRepository.findAll()){
            String addressName = address.getAddress();
            double[] latlong = getCoordinatesFromAddress(addressName);
            address.setLatitude(latlong[0]);
            address.setLongitude(latlong[1]);
            addressRepository.save(address);

        }
    }
}
