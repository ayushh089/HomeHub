package com.homehub_backend.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "resident_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resident {
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

    @Column(name = "apartment_number", length = 50)
    private String apartmentNumber;

    @ManyToOne
    @JoinColumn(name = "society_id")
    private Society society;

    @Column(name = "emergency_contact", length = 15)
    private String emergencyContact;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

}
