package com.example.entity;

import com.example.models.Payment;

import java.util.HashMap;
import java.util.Map;

public class PaymentDataStore {
    private static PaymentDataStore instance;
    private Map<String, Payment> payments = new HashMap<>();

    private PaymentDataStore() {
    }

    public static PaymentDataStore getInstance() {
        if (instance == null) {
            instance = new PaymentDataStore();
        }
        return instance;
    }

    public void addPayment(Payment payment) {
        payments.put(payment.getRideId(), payment);
    }

    public Payment getPayment(String rideId) {
        return payments.get(rideId);
    }
}