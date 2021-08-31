package com.epam.esm.entities;

import java.time.LocalDateTime;
import java.util.List;


/**
 * Stores data from "gift_certificate" table
 */
public class Certificate extends Entity {
    private static final long serialVersionUID = 128L;

    private List<Tag> tagList;
    private String name;
    private String description;
    private Integer price;
    private Integer duration;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;


    public Certificate() {
    }


    public Certificate(String name, String description, Integer price, Integer duration,
                       LocalDateTime createDate, LocalDateTime lastUpdateDate) {
        this.tagList = tagList;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
    }


    public List<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

}