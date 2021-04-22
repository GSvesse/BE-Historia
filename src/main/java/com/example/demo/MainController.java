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

    @PostMapping(path="/add")
    public @ResponseBody String addNewBild(@RequestParam String street, @RequestParam int year, @RequestParam String tag){
        Bilder b = new Bilder();
        b.setStreet(street);
        b.setYear(year);
        b.setTag(tag);
        bildRepository.save(b);
        return "Saved";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Bilder>getAllBilder(){
        return bildRepository.findAll();
    }
}
