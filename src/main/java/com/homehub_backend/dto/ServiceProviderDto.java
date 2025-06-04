package com.homehub_backend.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
public class ServiceProviderDto {

    private String email;
    private String phone;
    private String password;
    private String role;

    private String firstName;
    private String lastName;

    private String businessName;
    private String description;
    private Integer experienceYears;

    private Boolean isVerified;
    private LocalDateTime verificationDate;

    private BigDecimal rating;
    private Integer totalJobsCompleted;

    private Integer baseServiceCharge;

    private String phoneSecondary;

    private String address;
    private String city;
    private String state;
    private String pincode;

    private LocalTime availableHoursStart;
    private LocalTime availableHoursEnd;
    private Boolean isAvailable;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private UUID userRefId;


}
