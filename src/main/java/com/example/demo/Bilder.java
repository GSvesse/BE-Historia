package com.example.demo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Bilder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String street;

    private int year;

    private String tag;

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


}
