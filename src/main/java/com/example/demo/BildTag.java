
package com.example.demo;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class BildTag implements Serializable {
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