package com.epam.esm.comparators;

import com.epam.esm.entities.Certificate;

import java.util.Comparator;

public class CertificateComparator implements Comparator<Certificate> {
    @Override
    public int compare(Certificate o1, Certificate o2) {

        return o1.getName().compareTo(o2.getName()) |
                o1.getDescription().compareTo(o2.getDescription()) |
                o1.getDuration().compareTo(o2.getDuration()) |
                o1.getPrice().compareTo(o2.getPrice()) |
                o1.getCreateDate().compareTo(o2.getCreateDate()) |
                o1.getLastUpdateDate().compareTo(o2.getLastUpdateDate());
    }
}
