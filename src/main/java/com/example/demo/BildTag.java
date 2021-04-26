
package com.example.demo;

import javax.persistence.*;

@Entity(name = "bildTag")
public class BildTag {
    @Id
    private Integer bildId;

    private Integer tagId;

    public Integer getBildId() {
        return bildId;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setBildId(Integer bildId) {
        this.bildId = bildId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }
}