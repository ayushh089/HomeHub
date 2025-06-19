package com.homehub_backend.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "service_requests")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"mediaFiles", "statusHistory", "providerResponses", "rating"})

public class ServiceRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "resident_id", nullable = false)
    private UUID residentId;

    @Column(name = "provider_id")
    private UUID providerId;

    @Column(name = "society_id", nullable = false)
    private UUID societyId;

    @Column(name = "category_id", nullable = false)
    private UUID categoryId;



    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UrgencyLevel urgency = UrgencyLevel.MEDIUM;

    @Column(name = "preferred_date")
    private LocalDate preferredDate;

    @Column(name = "preferred_time_slot", length = 50)
    private String preferredTimeSlot;


    @Column(name = "location_details", columnDefinition = "TEXT")
    private String locationDetails;

    @Column(name = "contact_phone", length = 15)
    private String contactPhone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status ;



    @Column(name = "final_cost", precision = 10, scale = 2)
    private BigDecimal finalCost;

    @Column(name = "payment_method", length = 20)
    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;



    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "serviceRequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore

    private List<RequestMedia> mediaFiles = new ArrayList<>();

    @OneToMany(mappedBy = "serviceRequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore

    private List<RequestStatusHistory> statusHistory = new ArrayList<>();

    @OneToMany(mappedBy = "serviceRequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProviderResponse> providerResponses = new ArrayList<>();

    @OneToOne(mappedBy = "serviceRequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ServiceRating rating;


    public enum UrgencyLevel {
        LOW, MEDIUM, HIGH, EMERGENCY
    }

    public enum RequestStatus {
         SUBMITTED, PROVIDER_REVIEW, QUOTED, SCHEDULED,
        IN_PROGRESS, COMPLETED, CANCELLED, REJECTED, EXPIRED
    }

    public enum PaymentStatus {
        PENDING, PARTIAL, PAID, REFUNDED, FAILED
    }
}

