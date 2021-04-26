
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

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

}
