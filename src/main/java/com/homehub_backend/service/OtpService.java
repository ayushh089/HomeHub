package com.homehub_backend.service;

import com.homehub_backend.dao.OtpRepository;
import com.homehub_backend.dto.request.OtpRequest;
import com.homehub_backend.entity.UserOtp;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OtpService {

    @Autowired
    OtpRepository otpRepository;

    public void addOtp(OtpRequest otpRequest) {

        UserOtp newOtp = UserOtp.builder()
                .userId(otpRequest.getUserId())
                .otpCode(otpRequest.getOtpCode())
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .isUsed(false)
                .attempts(0)
                .createdAt(LocalDateTime.now())
                .build();

        otpRepository.save(newOtp);
        System.out.println("Saved OTp");

    }

    @Transactional
    public String verifyOtp(OtpRequest otpRequest) {
        Optional<UserOtp> otpForUser = otpRepository
                .findLatestOtpByUserId(otpRequest.getUserId());

        if (otpForUser.isEmpty()) {
            throw new IllegalArgumentException("No OTP found or already used.");
        }

        UserOtp userOtp = otpForUser.get();

        if (userOtp.isExpired()) {
            return  "OTP has expired.";
        }

        if (!userOtp.canAttempt(5)) {
            return "Maximum verification attempts exceeded.";
        }

        if (!userOtp.getOtpCode().equals(otpRequest.getOtpCode())) {
            userOtp.incrementAttempts();
            otpRepository.save(userOtp);
            return"Invalid OTP.";
        }

        userOtp.markAsUsed();
        otpRepository.save(userOtp);
        return "OK";
    }
}
