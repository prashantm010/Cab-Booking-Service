package com.example.factories;

import com.example.enums.CabType;
import com.example.interfaces.Cab;
import com.example.services.LuxuryCab;
import com.example.services.SharedCab;
import com.example.services.StandardCab;

public class CabFactory {
    public static Cab getCab(CabType cabType) {
        return switch (cabType) {
            case STANDARD -> new StandardCab();
            case LUXURY -> new LuxuryCab();
            case SHARED -> new SharedCab();
            default -> throw new IllegalArgumentException("Unknown cab type: " + cabType);
        };
    }
}