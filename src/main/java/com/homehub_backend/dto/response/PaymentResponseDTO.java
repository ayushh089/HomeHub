package com.homehub_backend.dto.response;


import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;

@Data
public class PaymentResponseDTO {
    private UUID transactionId;
    private String razorpayOrderId;
    private String amount;
    private String currency;
    private String status;
    private String paymentPageUrl;
    private LocalTime createdAt;

    // Getters and setters
}