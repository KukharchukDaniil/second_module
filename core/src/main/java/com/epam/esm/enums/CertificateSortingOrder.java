package com.epam.esm.enums;

import com.epam.esm.entities.Certificate;

import java.util.List;

/**
 * Provides {@link List<Certificate>} sorting functionality through implementing abstract method in each enum member
 */
public enum CertificateSortingOrder {
    NONE {
        @Override
        public void sort(List<Certificate> certificateList) {
        }
    },
    ASC {
        @Override
        public void sort(List<Certificate> certificateList) {
            certificateList.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
        }
    },
    DESC {
        @Override
        public void sort(List<Certificate> certificateList) {
            certificateList.sort((o1, o2) -> o2.getName().compareTo(o1.getName()));
        }
    };

    public abstract void sort(List<Certificate> certificateList);
}
