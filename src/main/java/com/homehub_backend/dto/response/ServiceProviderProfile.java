package com.homehub_backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ServiceProviderProfile {
    private UUID serviceProviderId;
    private String firstName;
    private String lastName;
    private String businessName;
    private String description;
    private Integer experienceYears;
    private Boolean isVerified;
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
    private List<ProviderCategory> categories;

    // Nested DTO for provider categories
    @Data
    @Builder
    public static class ProviderCategory {
        private UUID categoryId;
        private String categoryName;
        private Integer hourlyRate;
        private Integer minCharge;
        private Boolean isPrimary;

    }



}