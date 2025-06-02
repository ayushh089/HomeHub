package com.homehub_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "platform_admin_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlatformAdmin {
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

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
