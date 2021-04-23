package com.example.demo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
//hejhej
@Entity
public class Bilder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String street;

    private int year;

    private String tag;

    private String dokumentID;

    private String photographer;

    private String licence;

    public Integer getId(){
        return id;
    }

    public int getYear(){
        return year;
    }

    public String getStreet(){
        return street;
    }

    public String getTag(){
        return tag;
    }

    public String getdokumentID(){
        return dokumentID;
    }

    public String getPhotographer(){
        return photographer;
    }

    public String getLicence(){
        return licence;
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

    public void setTag(String tag){
        this.tag=tag;
    }

    public void setDokumentID(String dokumentID){
        this.dokumentID = dokumentID;
    }

    public void setPhotographer(String photographer){
        this.photographer = photographer;
    }

    public void setLicence(String licence){
        this.licence = licence;
    }


}
