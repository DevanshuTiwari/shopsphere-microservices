package com.dt.payment_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    @PostMapping
    public ResponseEntity<String> processPayment(@RequestBody Long orderId) {

//        Uncomment to test successful payment
//        System.out.println("Processing successful payment for order ID: " + orderId);
//        return ResponseEntity.ok("Payment successful for order " + orderId);

        System.out.println("Received payment request for order ID: " + orderId);
        // Randomly succeed or fail
        if (new java.util.Random().nextInt(10) < 7) { // 70% chance of success
            return ResponseEntity.ok("Payment successful for order " + orderId);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment service is currently unavailable");
        }
    }
}
