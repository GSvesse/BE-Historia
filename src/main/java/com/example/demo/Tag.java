
package com.example.demo;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer tagId;

    private String tag;

    @ManyToMany (mappedBy = "tags")
    Set<Bilder> bilder;

    public int getTagId() {
        return tagId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

}
