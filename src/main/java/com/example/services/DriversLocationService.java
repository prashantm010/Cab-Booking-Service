package com.example.services;

import com.example.entity.DriverLocationDataStore;
import com.example.models.DriverLocation;
import com.example.utils.GeoHashUtil;
import com.example.utils.HelperUtil;

import java.util.ArrayList;
import java.util.List;

public class DriversLocationService {
    private final DriverLocationDataStore driverLocationDataStore = DriverLocationDataStore.getInstance();

    public void updateDriverLocation(String driverId, double latitude, double longitude) {
        DriverLocation driverLocation = new DriverLocation(driverId, latitude, longitude);
        driverLocationDataStore.updateDriverLocation(driverLocation);
    }

    public List<DriverLocation> getNearByDrivers(double lat, double lon, double radiusKm) {
        List<String> geohashes = GeoHashUtil.getAdjacentGeohashes(lat, lon);
        List<DriverLocation> result = new ArrayList<>();

        for (String hash : geohashes) {
            List<DriverLocation> drivers = driverLocationDataStore.getDriverLocations(hash);
            for (DriverLocation loc : drivers) {
                if (HelperUtil.calculateDistance(lat, lon, loc.getLatitude(), loc.getLongitude()) <= radiusKm) {
                    result.add(loc);
                }
            }
        }
        return result;
    }
}
