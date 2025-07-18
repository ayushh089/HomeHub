package com.homehub_backend.dao;

import com.homehub_backend.entity.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, UUID> {

    Optional<PaymentTransaction> findByRazorpayOrderId(String razorpayOrderId);

    Optional<PaymentTransaction> findByRazorpayPaymentId(String razorpayPaymentId);

    // Explicit query for residentId
    @Query("SELECT pt FROM PaymentTransaction pt WHERE pt.resident.userId = :residentId")
    List<PaymentTransaction> findByResidentId(@Param("residentId") UUID residentId);

    // Explicit query for providerId
    @Query("SELECT pt FROM PaymentTransaction pt WHERE pt.provider.userId = :providerId")
    List<PaymentTransaction> findByProviderId(@Param("providerId") UUID providerId);

    // Explicit query for requestId
    @Query("SELECT pt FROM PaymentTransaction pt WHERE pt.request.id = :requestId")
    List<PaymentTransaction> findByRequestId(@Param("requestId") UUID requestId);

    List<PaymentTransaction> findByStatus(PaymentTransaction.PaymentStatus status);

    boolean existsByRazorpayOrderId(String razorpayOrderId);

    boolean existsByRazorpayPaymentId(String razorpayPaymentId);
}