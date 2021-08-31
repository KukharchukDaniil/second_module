package com.epam.esm.entities;

import java.io.Serializable;

/**
 * Stores ID field for data from "gift_certificate" and "tags" tables
 */
public abstract class Entity implements Serializable {
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Entity() {
    }

    public Entity(long id) {
        this.id = id;
    }

}
