package com.homehub_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;



@Entity
@Table(name = "provider_responses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProviderResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private ServiceRequest serviceRequest;

    @Column(name = "provider_id", nullable = false)
    private UUID providerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResponseType response;

    @Column(name = "proposed_cost", precision = 10, scale = 2)
    private BigDecimal proposedCost;

    @Column(name = "cost_breakdown", columnDefinition = "TEXT")
    private String costBreakdown;

    @Column(name = "proposed_date")
    private LocalDateTime proposedDate;

    @Column(name = "proposed_time_slot", length = 50)
    private String proposedTimeSlot;

    @Column(name = "estimated_duration")
    private Integer estimatedDuration;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "terms_conditions", columnDefinition = "TEXT")
    private String termsConditions;

    @Column(name = "valid_until")
    private LocalDateTime validUntil;

    @CreationTimestamp
    @Column(name = "responded_at", updatable = false)
    private LocalDateTime respondedAt;

    enum ResponseType {
        ACCEPTED, REJECTED, MODIFIED, QUOTED
    }
}
