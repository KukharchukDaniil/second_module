package com.epam.esm.enums;

import com.epam.esm.entities.Certificate;

import java.util.List;

public enum CertificateSortingOrder {
    NONE {
        @Override
        public List<Certificate> sort(List<Certificate> certificateList) {
            return certificateList;
        }
    },
    ASC {
        @Override
        public List<Certificate> sort(List<Certificate> certificateList) {
            certificateList.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
            return certificateList;
        }
    },
    DESC {
        @Override
        public List<Certificate> sort(List<Certificate> certificateList) {
            certificateList.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
            return certificateList;
        }
    };

    public abstract List<Certificate> sort(List<Certificate> certificateList);
}
