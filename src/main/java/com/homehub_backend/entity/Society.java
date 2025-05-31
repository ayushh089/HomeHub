    package com.homehub_backend.entity;

    import jakarta.persistence.*;
    import lombok.*;
    import org.hibernate.annotations.GenericGenerator;

    import java.time.LocalDateTime;
    import java.util.UUID;

    @Entity
    @Table(name = "society")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class Society {

        @Id
        @GeneratedValue(generator = "uuid2")
        @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
        @Column(columnDefinition = "uuid", updatable = false, nullable = false)
        private UUID id;

        @Column(nullable = false)
        private String name;

        @Column(nullable = false, columnDefinition = "TEXT")
        private String address;

        @Column(nullable = false, length = 100)
        private String city;

        @Column(nullable = false, length = 100)
        private String state;

        @Column(nullable = false, length = 10)
        private String pincode;

        @Column(name = "created_at", nullable = false)
        private LocalDateTime createdAt = LocalDateTime.now();
    }
