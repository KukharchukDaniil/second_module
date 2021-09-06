package com.epam.esm.entities;

import java.util.Objects;

/**
 * Stores data from "tags" table
 */
public class Tag extends Entity {

    private static final Long serialVersionUID = 1L;

    public Tag() {
    }

    public Tag(String name) {
        super(name);
    }

    public Tag(Long id, String name) {
        super(id, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tag)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Tag tag = (Tag) o;
        return Objects.equals(getName(), tag.getName()) && Objects.equals(getId(), tag.getId());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
