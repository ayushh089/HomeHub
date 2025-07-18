package com.homehub_backend.controller;

import com.homehub_backend.dto.request.PaymentRequestDTO;
import com.homehub_backend.dto.request.PaymentVerificationRequest;
import com.homehub_backend.dto.response.PaymentResponseDTO;
import com.homehub_backend.service.PaymentService;
import com.homehub_backend.service.UserPrincipal;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create-order")
    public ResponseEntity<PaymentResponseDTO> createPaymentOrder(
            @RequestBody PaymentRequestDTO paymentRequest
            ) throws RazorpayException {
        System.out.println(paymentRequest);

        PaymentResponseDTO response = paymentService.createPaymentOrder(paymentRequest);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody PaymentVerificationRequest verificationRequest) {

        System.out.println(verificationRequest);
        try {
            boolean isVerified = paymentService.verifyPayment(
                    verificationRequest.getRazorpayOrderId(),
                    verificationRequest.getRazorpayPaymentId(),
                    verificationRequest.getRazorpaySignature()
            );

            if (isVerified) {
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Payment verified successfully"
                ));
            } else {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Payment verification failed"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "success", false,
                    "message", "Error verifying payment: " + e.getMessage()
            ));
        }
    }

}