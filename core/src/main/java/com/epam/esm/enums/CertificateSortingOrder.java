package com.epam.esm.enums;

import com.epam.esm.entities.Certificate;

import java.util.List;

/**
 * Provides {@link List<Certificate>} sorting functionality through implementing abstract method in each enum member
 */
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
            if (certificateList != null) {
                certificateList.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
            }
            return certificateList;
        }
    },
    DESC {
        @Override
        public List<Certificate> sort(List<Certificate> certificateList) {
            if (certificateList != null) {
                certificateList.sort((o1, o2) -> o2.getName().compareTo(o1.getName()));
            }
            return certificateList;
        }
    };

    public abstract List<Certificate> sort(List<Certificate> certificateList);
}
