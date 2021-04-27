package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

//hejhej
@Controller
@RequestMapping(path="/demo")
public class MainController {
    @Autowired
    private BildRepository bildRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private AddressRepository addressRepository;

    @PostMapping(path="bilder/add")
    public @ResponseBody String addNewBild(@RequestParam String street, @RequestParam int year, @RequestParam String documentID, @RequestParam String photagrapher, @RequestParam String licence, @RequestParam String block, @RequestParam String district){
        Bilder b = new Bilder();
        b.setStreet(street);
        b.setYear(year);
        b.setDocumentID(documentID);
        b.setPhotographer(photagrapher);
        b.setLicence(licence);
        b.setBlock(block);
        b.setDistrict(district);
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
}
