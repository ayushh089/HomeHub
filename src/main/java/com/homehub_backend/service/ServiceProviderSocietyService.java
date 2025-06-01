package com.homehub_backend.service;

import com.homehub_backend.dao.AdminRepository;
import com.homehub_backend.dao.ServiceProviderRepository;
import com.homehub_backend.dao.SocietyRepository;
import com.homehub_backend.dao.ServiceProviderSocietyRepository;
import com.homehub_backend.dto.request.ServiceProviderSocietyRequest;
import com.homehub_backend.dto.response.ServiceProviderSocietyResponse;
import com.homehub_backend.entity.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ServiceProviderSocietyService {


    @Autowired
    private final ServiceProviderSocietyRepository serviceProviderSocietyRepository;
    @Autowired
    private final ServiceProviderRepository serviceProviderRepository;
    @Autowired
    private final SocietyRepository societyRepository;
    @Autowired
    private final AdminRepository adminRepository; // Back to AdminRepository
    private final ModelMapper modelMapper;

    public ResponseEntity<ServiceProviderSocietyResponse> addServiceProviderSociety(ServiceProviderSocietyRequest request) {

        ServiceProvider serviceProvider = serviceProviderRepository.findById(request.getServiceProviderId())
                .orElseThrow(() -> new RuntimeException("Service provider not found"));

        Society society = societyRepository.findById(request.getSocietyId())
                .orElseThrow(() -> new RuntimeException("Society not found"));

        Admin approvedByUser = null;
        if (request.getApprovedByUserId() != null) {
            approvedByUser = adminRepository.findById(request.getApprovedByUserId())
                    .orElseThrow(() -> new RuntimeException("Approver user not found"));
        }

        ServiceProviderSociety spSociety = ServiceProviderSociety.builder()
                .isPreferred(request.getIsPreferred() != null && request.getIsPreferred())
                .approvalStatus(ServiceProviderSociety.ApprovalStatus.valueOf(request.getApprovalStatus()))
                .approvedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .serviceProvider(serviceProvider)
                .society(society)
                .approvedBy(approvedByUser)
                .build();

        ServiceProviderSociety saved = serviceProviderSocietyRepository.save(spSociety);

        ServiceProviderSocietyResponse response = ServiceProviderSocietyResponse.builder()
                .id(saved.getId())
                .isPreferred(saved.getIsPreferred())
                .approvalStatus(saved.getApprovalStatus().name())
                .approvedAt(saved.getApprovedAt())
                .createdAt(saved.getCreatedAt())
                .serviceProviderId(serviceProvider.getUserId())
                .societyId(society.getId())
                .approvedByUserId(approvedByUser != null ? approvedByUser.getUserId() : null)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}