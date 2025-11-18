package com.accenture.sb4.features;

import org.springframework.resilience.annotation.ConcurrencyLimit;
import org.springframework.resilience.annotation.EnableResilientMethods;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@EnableResilientMethods
public class PaymentService {

    @Retryable(maxAttempts = 3)
    public String processPayment(double amount) {
        if (Math.random() < 0.7) {
            throw new RuntimeException("Payment gateway error");
        }
        return "Payment of $" + amount + " processed successfully";
    }

    @ConcurrencyLimit(limit = 5)
    public String checkPaymentStatus(String paymentId) {
        return "Status of payment " + paymentId + ": COMPLETED";
    }
}
