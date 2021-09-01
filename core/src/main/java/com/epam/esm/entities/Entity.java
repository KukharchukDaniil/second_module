package com.epam.esm.entities;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity)) return false;
        Entity entity = (Entity) o;
        return id == entity.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
