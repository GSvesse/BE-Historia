package com.example.demo;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer addressId;

    private String address;

    @ManyToMany (mappedBy = "addresses")
    Set<Bilder> bilder;

    public String getAddress(){
        return address;
    }

    public void setAddress(String address){
        this.address = address;
    }
}
