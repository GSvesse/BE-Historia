package com.example.demo;

import javax.persistence.*;
import java.util.Set;

//hejhej
@Entity(name = "bilder")
public class Bilder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] image;

    private int year;

    private String description;

    private String documentID;

    private String photographer;

    private String licence;

    private String block;

    private String district;

//    @ManyToMany
//    @JoinTable(
//            name = "bilder_tag",
//            joinColumns = @JoinColumn(name = "bild_id"),
//            inverseJoinColumns = @JoinColumn(name = "tag_id")
//    )
//    Set<Tag> tags;
//
//    @ManyToMany
//    @JoinTable(
//            name = "bilder_address",
//            joinColumns = @JoinColumn(name = "bild_id"),
//            inverseJoinColumns = @JoinColumn(name = "address_id")
//    )
//    Set<Address> addresses;

    public Integer getId(){
        return id;
    }

    public byte[] getImage(){
        return image;
    }

    public int getYear(){
        return year;
    }

    public String getDescription(){
        return description;
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

//    public Set<Tag> getTags(){
//        return tags;
//    }
//
//    public Set<Address> getAddresses(){
//        return addresses;
//    }

    public void setId(Integer id){
        this.id=id;
    }

    public void setImage(byte[] image){
        this.image = image;
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

//    public void setTags(Set<Tag> tags){
//        this.tags = tags;
//    }
//
//    public void setAddresses(Set<Address> addresses){
//        this.addresses = addresses;
//    }



}
