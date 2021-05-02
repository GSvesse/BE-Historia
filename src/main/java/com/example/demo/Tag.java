package com.example.demo;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer tagId;

    private String tag;

    @ManyToMany (mappedBy = "tags")
    private Set<Bilder> bilder;

//    public int getTagId() {
//        return tagId;
//    }

    public String getTag() {
        return tag;
    }

    public Set<Bilder> getBilder(){
        bilder = new HashSet<>();
        return bilder;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

}