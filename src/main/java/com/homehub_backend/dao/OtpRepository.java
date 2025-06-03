package com.homehub_backend.dao;

import com.homehub_backend.entity.UserOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface OtpRepository extends JpaRepository<UserOtp, UUID> {

    @Query("SELECT u FROM UserOtp u WHERE u.userId = :userId " +
            "ORDER BY u.createdAt DESC LIMIT 1")
    Optional<UserOtp> findLatestOtpByUserId(@Param("userId") UUID userId);
}
