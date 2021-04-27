
package com.example.demo;

import javax.persistence.*;

@Entity(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer tagId;

    private String tag;

    public int getTagId() {
        return tagId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    //@OneToOne(mappedBy = "tagid")
    //private BildTag bildTag2;


}
