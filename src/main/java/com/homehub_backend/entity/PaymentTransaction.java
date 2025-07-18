package com.homehub_backend.entity;

import com.homehub_backend.dto.response.ServiceProviderProfile;
import com.homehub_backend.entity.Resident;
import com.homehub_backend.entity.ServiceRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "payment_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentTransaction {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private ServiceRequest request;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resident_id", nullable = false)
    private Resident resident;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id")
    private ServiceProvider provider;

    @Column(name = "razorpay_order_id", unique = true)
    private String razorpayOrderId;

    @Column(name = "razorpay_payment_id", unique = true)
    private String razorpayPaymentId;

    @Column(name = "razorpay_signature")
    private String razorpaySignature;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "currency", length = 3)
    private String currency = "INR";

    @Column(name = "gateway_fee", precision = 10, scale = 2)
    private BigDecimal gatewayFee;

    @Column(name = "tax", precision = 10, scale = 2)
    private BigDecimal tax;

    @Column(name = "net_amount", precision = 10, scale = 2)
    private BigDecimal netAmount;

    @Column(name = "commission", precision = 10, scale = 2)
    private BigDecimal commission = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", length = 20)
    private PaymentMethod paymentMethod;

    @Column(name = "payment_method_detail", length = 100)
    private String paymentMethodDetail;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private PaymentStatus status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalTime updatedAt;

    @Column(name = "expires_at")
    private LocalTime expiresAt;

    @Column(name = "captured_at")
    private LocalTime capturedAt;

    public enum PaymentMethod {
        UPI, CARD, NETBANKING, WALLET, EMI
    }

    public enum PaymentStatus {
        CREATED,
        ATTEMPTED,
        PAID,
        CAPTURED,
        FAILED,
        REFUNDED,
        PARTIALLY_REFUNDED,
        ON_HOLD,
        DISPUTED
    }
}