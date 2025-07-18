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

    @Column(name = "total_cost", columnDefinition = "TEXT")
    private String totalCost;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "responded_at", updatable = false)
    private LocalDateTime respondedAt;

    public enum ResponseType {
        ACCEPTED,
        REJECTED,
        MODIFIED,
        QUOTED,
        OUT_FOR_SERVICE,
        COMPLETED
    }
}