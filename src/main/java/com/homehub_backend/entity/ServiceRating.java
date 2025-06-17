package com.homehub_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "service_ratings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRating {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private ServiceRequest serviceRequest;

    @Column(name = "resident_id", nullable = false)
    private UUID residentId;

    @Column(name = "provider_id", nullable = false)
    private UUID providerId;

    @Column(name = "overall_rating", nullable = false)
    private Integer overallRating;

    @Column(name = "quality_rating")
    private Integer qualityRating;

    @Column(name = "timeliness_rating")
    private Integer timelinessRating;

    @Column(name = "professionalism_rating")
    private Integer professionalismRating;

    @Column(name = "value_rating")
    private Integer valueRating;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    @Column(name = "would_recommend")
    private Boolean wouldRecommend;

    @Column(name = "service_completed_on_time")
    private Boolean serviceCompletedOnTime;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}