package com.epam.esm.entities;

import java.io.Serializable;

public abstract class Entity implements Serializable {
    private long id; // version id

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
