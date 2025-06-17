package com.homehub_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "request_status_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private ServiceRequest serviceRequest;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_status")
    private ServiceRequest.RequestStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_status", nullable = false)
    private ServiceRequest.RequestStatus toStatus;

    @Column(name = "changed_by")
    private UUID changedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "changed_by_type")
    private UserType changedByType;

    @Column(length = 100)
    private String reason;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

   public enum UserType {
        RESIDENT, PROVIDER, ADMIN, SYSTEM
    }
}