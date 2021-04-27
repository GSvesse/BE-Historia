package com.example.demo;

import javax.persistence.*;
import java.util.Set;

//hejhej
@Entity(name = "bilder")
public class Bilder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String street;

    private int year;

    private String description;

    private String documentID;

    private String photographer;

    private String licence;

    private String block;

    private String district;

    @ManyToMany
    @JoinTable(
            name = "bilder_tag",
            joinColumns = @JoinColumn(name = "bild_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    Set<Tag> tags;

    public Integer getId(){
        return id;
    }

    public int getYear(){
        return year;
    }

    public String getDescription(){
        return description;
    }

    public String getStreet(){
        return street;
    }

    public String getDocumentID(){
        return documentID;
    }

    public String getPhotographer(){
        return photographer;
    }

    public String getLicence(){
        return licence;
    }

    public String getBlock(){
        return block;
    }

    public String getDistrict(){
        return district;
    }

    public void setId(Integer id){
        this.id=id;
    }

    public void setStreet(String street){
        this.street=street;
    }

    public void setYear(int year){
        this.year=year;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setDocumentID(String documentID){
        this.documentID = documentID;
    }

    public void setPhotographer(String photographer){
        this.photographer = photographer;
    }

    public void setLicence(String licence){
        this.licence = licence;
    }

    public void setBlock(String block){
        this.block = block;
    }

    public void setDistrict(String district){
        this.district = district;
    }


}
