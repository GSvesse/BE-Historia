package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.Set;

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

    @PostMapping(path="bilder/add")
    public @ResponseBody String addNewBild(@RequestParam("image")MultipartFile multipartFile, @RequestParam int year, @RequestParam String documentID, @RequestParam String photographer, @RequestParam String licence, @RequestParam String block, @RequestParam String district, @RequestParam String description) throws IOException {
        Bilder b = new Bilder();
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        b.setImage(fileName.getBytes());
        b.setYear(year);
        //b.setAddresses(addresses);
        //b.setTags(tags);
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

    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Integer id) {
        Bilder bild = bildRepository.findById(id).get();


        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + bild.getId() + "\"")
                .body(bild.getImage());
    }
}
