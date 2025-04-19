package com.example.utils;

import ch.hsr.geohash.GeoHash;

import java.util.ArrayList;
import java.util.List;

public class GeoHashUtil {
    public static final Integer PRECISION = 5;

    public static String encode(Double lat, Double lon) {
        return GeoHash.withCharacterPrecision(lat, lon, PRECISION).toBase32();
    }

    public static List<String> getAdjacentGeohashes(Double lat, Double lon) {
        GeoHash center = GeoHash.withCharacterPrecision(lat, lon, PRECISION);
        List<String> neighbors = new ArrayList<>();
        for (GeoHash n : center.getAdjacent()) {
            neighbors.add(n.toBase32());
        }
        neighbors.add(center.toBase32()); // Include center
        return neighbors;
    }
}
