package com.epam.esm.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Stores data from "gift_certificate" table
 */
public class Certificate extends Entity {
    private static final Long serialVersionUID = 128L;

    private List<Tag> tagList;
    private String description;
    private BigDecimal price;
    private Integer duration;

    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;

    public Certificate() {
    }

    public Certificate(String name, String description, BigDecimal price, Integer duration,
                       LocalDateTime createDate, LocalDateTime lastUpdateDate) {
        super(name);
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
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

    @JsonIgnore
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    @JsonIgnore
    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Certificate)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Certificate that = (Certificate) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(tagList, that.tagList)
                && Objects.equals(getName(), that.getName()) && Objects.equals(description, that.description)
                && Objects.equals(price, that.price) && Objects.equals(duration, that.duration)
                && Objects.equals(createDate, that.createDate) && Objects.equals(lastUpdateDate, that.lastUpdateDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tagList, description, price, duration, createDate, lastUpdateDate);
    }
}