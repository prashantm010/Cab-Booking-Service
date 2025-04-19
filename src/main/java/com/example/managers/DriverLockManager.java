package com.example.managers;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class DriverLockManager {
    private static DriverLockManager instance;
    private final ConcurrentHashMap<String, ReentrantLock> driverLocks = new ConcurrentHashMap<>();

    private DriverLockManager() {

    }

    public static DriverLockManager getInstance() {
        if (instance == null) {
            instance = new DriverLockManager();
        }
        return instance;
    }

    public ReentrantLock getDriverLock(String driverId) {
        driverLocks.putIfAbsent(driverId, new ReentrantLock());
        return driverLocks.get(driverId);
    }
}
