package com.example.demo;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer addressId;

    private String address;
    private double latitude;
    private double longitude;

    @ManyToMany (mappedBy = "addresses")
    Set<Bilder> bilder;

    public String getAddress(){
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Set<Bilder> getBilder(){
        bilder = new HashSet<>();
        return bilder;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}