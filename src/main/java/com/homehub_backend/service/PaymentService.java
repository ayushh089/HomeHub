package com.homehub_backend.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.homehub_backend.dao.PaymentTransactionRepository;
import com.homehub_backend.dao.ResidentRepository;
import com.homehub_backend.dao.ServiceProviderRepository;
import com.homehub_backend.dao.ServiceRequestRepository;
import com.homehub_backend.dto.request.PaymentRequestDTO;
import com.homehub_backend.dto.response.PaymentResponseDTO;
import com.homehub_backend.dto.response.ServiceProviderProfile;
import com.homehub_backend.entity.Resident;
import com.homehub_backend.entity.ServiceProvider;
import com.homehub_backend.entity.ServiceRequest;
import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.HmacUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.homehub_backend.entity.PaymentTransaction;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.Arrays;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {

    private final RazorpayClient razorpayClient;
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final ServiceRequestRepository serviceRequestRepository;
    private final ResidentRepository residentProfileRepository;
    private final ServiceProviderRepository providerProfileRepository;
    private final ResidentRepository residentRepository;
    private final ServiceProviderRepository serviceProviderRepository;

    @Value("${razorpay.api.key}")
    private String razorpayKeyId;
    @Value("${razorpay.api.secret}")
    private String razorpayKeySecret;




    public PaymentResponseDTO createPaymentOrder(PaymentRequestDTO paymentRequest) throws RazorpayException {
        ServiceRequest request = serviceRequestRepository.findById(paymentRequest.getRequestId())
                .orElseThrow(() -> new NotFoundException("Service request not found"));

        Resident resident = residentRepository.findById(paymentRequest.getResidentId())
                .orElseThrow(() -> new NotFoundException("Resident not found"));

        ServiceProvider provider = serviceProviderRepository.findById(request.getProviderId())
                .orElseThrow(() -> new NotFoundException(
                        "Service provider not found with id: " + request.getProviderId()));


        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", paymentRequest.getAmount().multiply(new BigDecimal(100)).intValue());
        orderRequest.put("currency", paymentRequest.getCurrency());
        orderRequest.put("receipt",  paymentRequest.getRequestId());

        JSONObject notes = new JSONObject();
        notes.put("request_id", paymentRequest.getRequestId().toString());
        notes.put("resident_id", paymentRequest.getResidentId().toString());
        orderRequest.put("notes", notes);

        System.out.println("Order request: " + orderRequest.toString());
        Order order = razorpayClient.orders.create(orderRequest);
        System.out.println("Order response: " + order.toString()); // Check if this executes

        PaymentTransaction transaction = new PaymentTransaction();
        transaction.setRequest(request);
        transaction.setResident(resident);

        transaction.setProvider(provider);
        transaction.setAmount(paymentRequest.getAmount());
        transaction.setCurrency(paymentRequest.getCurrency());
        transaction.setRazorpayOrderId(order.get("id"));
        transaction.setStatus(PaymentTransaction.PaymentStatus.CREATED);
        transaction = paymentTransactionRepository.save(transaction);

        PaymentResponseDTO response = new PaymentResponseDTO();
        response.setTransactionId(transaction.getId());
        response.setRazorpayOrderId(order.get("id"));
        response.setAmount(paymentRequest.getAmount().toString());
        response.setCurrency(paymentRequest.getCurrency());
        response.setStatus("CREATED");
        response.setPaymentPageUrl(buildPaymentPageUrl(order.get("id"),paymentRequest.getAmount()));
        response.setCreatedAt(transaction.getCreatedAt());

        return response;


    }

    private String buildPaymentPageUrl(String orderId, BigDecimal amount) {
        String businessName = "Your Business Name";
        String paymentDescription = "Payment for services";

        return String.format(
                "https://checkout.razorpay.com/v1?key=%s&amount=%d&currency=INR&order_id=%s&name=%s&description=%s",
                razorpayKeyId,
                amount.multiply(new BigDecimal(100)).intValue(),
                orderId,
                URLEncoder.encode(businessName, StandardCharsets.UTF_8),
                URLEncoder.encode(paymentDescription, StandardCharsets.UTF_8)
        );
    }

    public boolean verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) throws RazorpayException {
        try {
            // Verify the payment signature
            String verificationData = razorpayOrderId + "|" + razorpayPaymentId;
            String generatedSignature = HmacUtils.hmacSha256Hex(razorpayKeySecret, verificationData);

            if (!generatedSignature.equals(razorpaySignature)) {
                throw new SecurityException("Payment signature verification failed");
            }

            Payment payment = razorpayClient.payments.fetch(razorpayPaymentId);

            PaymentTransaction transaction = paymentTransactionRepository.findByRazorpayOrderId(razorpayOrderId)
                    .orElseThrow(() -> new NotFoundException("Transaction not found"));

            transaction.setRazorpayPaymentId(razorpayPaymentId);
            transaction.setRazorpaySignature(razorpaySignature);
            transaction.setStatus(PaymentTransaction.PaymentStatus.PAID);
            transaction.setPaymentMethod(PaymentTransaction.PaymentMethod.CARD);
            transaction.setPaymentMethodDetail(payment.get("bank") != null ? payment.get("bank").toString() : null);
            transaction.setCapturedAt(LocalTime.now());

            BigDecimal amount = new BigDecimal(payment.get("amount").toString()).divide(new BigDecimal(100));
            BigDecimal gatewayFee = amount.multiply(new BigDecimal("0.02")); // 2% gateway fee
            BigDecimal tax = gatewayFee.multiply(new BigDecimal("0.18")); // 18% tax on gateway fee
            BigDecimal netAmount = amount.subtract(gatewayFee).subtract(tax);

            transaction.setAmount(amount);
            transaction.setGatewayFee(gatewayFee);
            transaction.setTax(tax);
            transaction.setNetAmount(netAmount);

            paymentTransactionRepository.save(transaction);

            ServiceRequest request = transaction.getRequest();
            request.setPaymentStatus(ServiceRequest.PaymentStatus.PAID);
            serviceRequestRepository.save(request);

            return true;
        } catch (Exception e) {
            PaymentTransaction transaction = paymentTransactionRepository.findByRazorpayOrderId(razorpayOrderId)
                    .orElse(null);
            if (transaction != null) {
                transaction.setStatus(PaymentTransaction.PaymentStatus.FAILED);
                paymentTransactionRepository.save(transaction);
            }

            throw e;
        }
    }


}