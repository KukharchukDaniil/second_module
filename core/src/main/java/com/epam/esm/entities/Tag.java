package com.epam.esm.entities;

/**
 * Stores data from "tags" table
 */
public class Tag extends Entity {

    private static final long serialVersionUID = 1L;

    private String name;

    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }

    public Tag(long id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
