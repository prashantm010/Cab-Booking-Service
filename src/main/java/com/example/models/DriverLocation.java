package com.example.models;

import com.example.utils.GeoHashUtil;
import com.example.utils.IdGenerator;
import lombok.Data;

import java.time.Instant;

@Data
public class DriverLocation {
    String locationId;
    String driverId;
    Double latitude;
    Double longitude;
    Long timestamp;
    String geoHash;

    public DriverLocation(String driverId, Double latitude, Double longitude) {
        this.locationId = IdGenerator.getId();
        this.driverId = driverId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = Instant.now().getEpochSecond();
        this.geoHash = GeoHashUtil.encode(latitude, longitude);
    }
}
