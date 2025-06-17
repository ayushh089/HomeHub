package com.homehub_backend.entity;

import jakarta.persistence.Entity;
import lombok.*;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Table(name = "request_media")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private ServiceRequest serviceRequest;

    @Column(nullable = false, length = 500)
    private String url;

    @Column(length = 255)
    private String filename;

    @Column(name = "file_size")
    private Integer fileSize;

    @Enumerated(EnumType.STRING)
    @Column(name = "media_type", nullable = false)
    private MediaType mediaType;

    @Column(name = "mime_type", length = 100)
    private String mimeType;

    @Column(name = "is_before_service")
    private Boolean isBeforeService = true;

    @Column(name = "uploaded_by")
    private UUID uploadedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "uploaded_by_type")
    private UserType uploadedByType;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum MediaType {
        IMAGE, VIDEO, DOCUMENT, AUDIO
    }

    public enum UserType {
        RESIDENT, PROVIDER
    }
}
