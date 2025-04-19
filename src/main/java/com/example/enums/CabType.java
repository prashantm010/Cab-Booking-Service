package com.example.enums;

public enum CabType {
    LUXURY,
    STANDARD,
    SHARED;

    public static CabType fromString(String cabType) {
        for (CabType type : CabType.values()) {
            if (type.name().equalsIgnoreCase(cabType)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + cabType);
    }
}
