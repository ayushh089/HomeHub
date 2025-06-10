package com.homehub_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceProviderApprovalReqResponse {

    private UUID userId;
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

    private List<ServiceCategoryInfo> services;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ServiceCategoryInfo {
        private UUID id;
        private String categoryName;
        private Integer hourlyRate;
        private Integer minCharge;
        private Boolean isPrimary;
        private LocalDateTime createdAt;
    }
}
