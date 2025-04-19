package com.example.managers;

import lombok.Getter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class PaymentLockManager {
    @Getter
    private static final PaymentLockManager instance = new PaymentLockManager();
    private final ConcurrentHashMap<String, ReentrantLock> ridePaymentLocks = new ConcurrentHashMap<>();

    private PaymentLockManager() {}

    public ReentrantLock getLock(String rideId) {
        ridePaymentLocks.putIfAbsent(rideId, new ReentrantLock());
        return ridePaymentLocks.get(rideId);
    }
}