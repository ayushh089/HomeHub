package com.homehub_backend.dto.request;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class PaymentVerificationRequest {
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;
    private String requestId;

    // Getters and setters
}