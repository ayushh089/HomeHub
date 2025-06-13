package com.homehub_backend.service;

import com.homehub_backend.dao.*;
import com.homehub_backend.dto.ServiceProviderDto;
import com.homehub_backend.dto.UserDto;
import com.homehub_backend.dto.response.ProfileResponse;
import com.homehub_backend.dto.response.ServiceProviderProfile;
import com.homehub_backend.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class ServiceProviderService {

    @Autowired
    ServiceProviderRepository serviceProviderRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    SocietyRepository societyRepository;
    @Autowired
    SocietyService societyService;

    @Autowired
    ServiceProviderSocietyRepository serviceProviderSocietyRepository;

    @Autowired
    ServiceProviderCategoryRepository serviceProviderCategoryRepository;


    public ResponseEntity<ProfileResponse> createServiceProvider(UUID userId, ServiceProviderDto dto) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        System.out.println("vkbdsjbvbjdsbvjb");

        ServiceProvider newProvider = ServiceProvider.builder()
                .user(user)
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .businessName(dto.getBusinessName())
                .description(dto.getDescription())
                .experienceYears(dto.getExperienceYears() != null ? dto.getExperienceYears() : 0)
                .isVerified(dto.getIsVerified() != null ? dto.getIsVerified() : false)
                .verificationDate(dto.getVerificationDate())
                .rating(dto.getRating() != null ? dto.getRating() : BigDecimal.ZERO)
                .totalJobsCompleted(dto.getTotalJobsCompleted() != null ? dto.getTotalJobsCompleted() : 0)
                .baseServiceCharge(dto.getBaseServiceCharge())
                .phoneSecondary(dto.getPhoneSecondary())
                .address(dto.getAddress())
                .city(dto.getCity())
                .state(dto.getState())
                .pincode(dto.getPincode())
                .availableHoursStart(dto.getAvailableHoursStart())
                .availableHoursEnd(dto.getAvailableHoursEnd())
                .isAvailable(dto.getIsAvailable() != null ? dto.getIsAvailable() : true)
                .createdAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDateTime.now())
                .updatedAt(dto.getUpdatedAt() != null ? dto.getUpdatedAt() : LocalDateTime.now())
                .build();


        ServiceProvider savedProvider = serviceProviderRepository.save(newProvider);
        System.out.println("hy");

        return ResponseEntity.ok(ProfileResponse.complete());


    }

    public ResponseEntity<List<ServiceProviderProfile>> getListBySocietyId(UUID societyId, UUID category) {
        try {
            // Get approved service providers for the society
            List<ServiceProviderSociety> serviceProviderList = serviceProviderSocietyRepository
                    .findBySocietyIdAndApprovalStatus(societyId, ServiceProviderSociety.ApprovalStatus.APPROVED);

            List<ServiceProviderProfile> result = new ArrayList<>();

            for (ServiceProviderSociety sps : serviceProviderList) {
                ServiceProvider serviceProvider = sps.getServiceProvider();
                Users user=userRepository.findById(serviceProvider.getUserId())
                        .orElseThrow(() -> new RuntimeException("User not found with ID: " + serviceProvider.getUserId()));


                // Get categories - filtered if category parameter is provided
                List<ServiceProviderCategory> spCategories;
                if (category == null) {
                    spCategories = serviceProviderCategoryRepository.findByServiceProviderId(serviceProvider.getUserId());
                } else {
                    spCategories = serviceProviderCategoryRepository
                            .findByServiceProviderIdAndCategoryId(serviceProvider.getUserId(), category);
                }

                if (category != null && spCategories.isEmpty()) {
                    continue;
                }

                List<ServiceProviderProfile.ProviderCategory> providerCategories = spCategories.stream()
                        .map(spc -> ServiceProviderProfile.ProviderCategory.builder()
                                .categoryId(spc.getCategory().getId())
                                .categoryName(spc.getCategory().getName()) // Assuming ServiceCategory has getName()
                                .hourlyRate(spc.getHourlyRate())
                                .minCharge(spc.getMinCharge())
                                .isPrimary(spc.getIsPrimary())
                                .build())
                        .collect(Collectors.toList());

                // Build the ServiceProviderProfile
                ServiceProviderProfile profile = ServiceProviderProfile.builder()
                        .serviceProviderId(serviceProvider.getUserId())
                        .firstName(serviceProvider.getFirstName())
                        .lastName(serviceProvider.getLastName())
                        .businessName(serviceProvider.getBusinessName())
                        .description(serviceProvider.getDescription())
                        .experienceYears(serviceProvider.getExperienceYears())
                        .isVerified(serviceProvider.getIsVerified())
                        .rating(serviceProvider.getRating())
                        .totalJobsCompleted(serviceProvider.getTotalJobsCompleted())
                        .baseServiceCharge(serviceProvider.getBaseServiceCharge())
                        .phoneSecondary(serviceProvider.getPhoneSecondary())
                        .address(serviceProvider.getAddress())
                        .city(serviceProvider.getCity())
                        .state(serviceProvider.getState())
                        .pincode(serviceProvider.getPincode())
                        .availableHoursStart(serviceProvider.getAvailableHoursStart())
                        .availableHoursEnd(serviceProvider.getAvailableHoursEnd())
                        .phone(user.getPhone())
                        .email(user.getEmail())
                        .isAvailable(serviceProvider.getIsAvailable())
                        .categories(providerCategories)
                        .build();

                result.add(profile);
            }
            System.out.println(result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
//            log.error("Error fetching service providers for society: " + societyId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
