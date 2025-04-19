package com.example.services;

import com.example.entity.PaymentDataStore;
import com.example.enums.PaymentStatus;
import com.example.exceptions.PaymentAlreadyMadeException;
import com.example.exceptions.PaymentServiceFailureException;
import com.example.models.Payment;
import com.example.managers.PaymentLockManager;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class PaymentService {
    private final RideService rideService = new RideService();
    private final PaymentDataStore paymentDataStore = PaymentDataStore.getInstance();
    private static final long LOCK_TIMEOUT_MS = 100;

    public Payment makePayment(String rideId, double amount) {
        ReentrantLock lock = PaymentLockManager.getInstance().getLock(rideId);
        boolean acquired = false;
        try {
            acquired = lock.tryLock(LOCK_TIMEOUT_MS, TimeUnit.MILLISECONDS);
            if (!acquired) {
                throw new PaymentServiceFailureException("Could not acquire lock for rideId: " + rideId);
            }
            Payment payment = PaymentDataStore.getInstance().getPayment(rideId);
            if (Objects.nonNull(payment) && PaymentStatus.PAID.equals(payment.getStatus())) {
                throw new PaymentAlreadyMadeException("Payment already made for rideId: " + rideId);
            }
            payment = new Payment(rideId, amount, PaymentStatus.PAID);
            paymentDataStore.addPayment(payment);
            rideService.markRideCompleted(rideId);
            return payment;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PaymentServiceFailureException("Thread interrupted while trying to acquire lock " + e.getMessage());
        } finally {
            if (acquired) {
                lock.unlock();
            }
        }
    }
}