package com.homehub_backend.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "service_provider_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"user", "categories", "societies"}) // Add this

public class ServiceProvider {
    @Id
    private UUID userId;

    @OneToOne
    @MapsId // Shares the same primary key as User
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "business_name", length = 200)
    private String businessName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "experience_years")
    private Integer experienceYears = 0;

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    @Column(name = "verification_date")
    private LocalDateTime verificationDate;

    @Column(precision = 3, scale = 2)
    private BigDecimal rating = BigDecimal.ZERO;

    @Column(name = "total_jobs_completed")
    private Integer totalJobsCompleted = 0;

    @Column(name = "base_service_charge", precision = 10, scale = 2)
    private Integer baseServiceCharge;

    @Column(name = "phone_secondary", length = 10)
    private String phoneSecondary;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(length = 10)
    private String pincode;

    @Column(name = "available_hours_start")
    private LocalTime availableHoursStart;

    @Column(name = "available_hours_end")
    private LocalTime availableHoursEnd;

    @Column(name = "is_available")
    private Boolean isAvailable = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;



    @OneToMany(mappedBy = "serviceProvider", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ServiceProviderCategory> categories = new ArrayList<>();

    @OneToMany(mappedBy = "serviceProvider", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ServiceProviderSociety> societies = new ArrayList<>();


}
