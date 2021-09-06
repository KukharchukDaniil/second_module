package com.epam.esm.entities;

import java.io.Serializable;
import java.util.Objects;

/**
 * Stores ID field for data from "gift_certificate" and "tags" tables
 */
public abstract class Entity implements Serializable {
    private Long id;

    private String name;

    public Entity() {
    }

    public Entity(String name) {
        this.name = name;
    }

    public Entity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Entity)) {
            return false;
        }
        Entity entity = (Entity) o;
        return id == entity.id && Objects.equals(name, entity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
