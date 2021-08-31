package com.epam.esm.comparators;

import com.epam.esm.entities.Tag;

import java.util.Comparator;

public class TagComparator implements Comparator<Tag> {
    @Override
    public int compare(Tag o1, Tag o2) {
        return o1.getName().compareTo(o2.getName()) | (o1.getId() == o2.getId() ? 0 : 1);
    }
}
