package com.homehub_backend.service;

import com.homehub_backend.dao.*;
import com.homehub_backend.dto.ServiceProviderDto;
import com.homehub_backend.dto.request.ApprovalRequest;
import com.homehub_backend.dto.request.ServiceProviderSocietyRequest;
import com.homehub_backend.dto.response.ServiceProviderApprovalReqResponse;
import com.homehub_backend.dto.response.ServiceProviderProfile;
import com.homehub_backend.dto.response.ServiceProviderSocietyResponse;
import com.homehub_backend.entity.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private final AdminRepository adminRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ServiceProviderCategoryRepository serviceProviderCategoryRepository;

    private final ModelMapper modelMapper;

    public ResponseEntity<ServiceProviderSocietyResponse> addServiceProviderSociety(ServiceProviderSocietyRequest request) {


        System.out.println(request);
        ServiceProvider serviceProvider = serviceProviderRepository.findById(request.getServiceProviderId())
                .orElseThrow(() -> new RuntimeException("Service provider not found"));

        Society society = societyRepository.findById(request.getSocietyId())
                .orElseThrow(() -> new RuntimeException("Society not found"));


        ServiceProviderSociety spSociety = ServiceProviderSociety.builder()
                .isPreferred(request.getIsPreferred() != null && request.getIsPreferred())
                .approvalStatus(
                        request.getApprovalStatus() != null
                                ? ServiceProviderSociety.ApprovalStatus.valueOf(request.getApprovalStatus())
                                : ServiceProviderSociety.ApprovalStatus.PENDING // <-- or whatever default you want
                )
                .createdAt(LocalDateTime.now())
                .serviceProvider(serviceProvider)
                .society(society)
                .build();

        ServiceProviderSociety saved = serviceProviderSocietyRepository.save(spSociety);

        ServiceProviderSocietyResponse response = ServiceProviderSocietyResponse.builder()
                .id(saved.getId())
                .isPreferred(saved.getIsPreferred())
                .approvalStatus(saved.getApprovalStatus().name())
                .createdAt(saved.getCreatedAt())
                .serviceProviderId(serviceProvider.getUserId())
                .societyId(society.getId())
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public List<ServiceProviderApprovalReqResponse> getServiceProviderRequestsBySocietyId(UUID societyId) {
        List<ServiceProviderSociety> requests = serviceProviderSocietyRepository.findBySocietyIdAndApprovalStatus(societyId, ServiceProviderSociety.ApprovalStatus.PENDING);
        return mapToResponseList(requests);
    }

    private List<ServiceProviderApprovalReqResponse> mapToResponseList(List<ServiceProviderSociety> requests) {
        return requests.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ServiceProviderApprovalReqResponse mapToResponse(ServiceProviderSociety request) {
        // Get service provider details
        ServiceProvider provider = serviceProviderRepository.findById(request.getServiceProvider().getUserId())
                .orElseThrow(() -> new RuntimeException("Service provider not found"));

        // Get service categories for this provider
        List<ServiceProviderCategory> categories = serviceProviderCategoryRepository
                .findByServiceProviderUserId(request.getServiceProvider().getUserId());

        List<ServiceProviderApprovalReqResponse.ServiceCategoryInfo> services = categories.stream()
                .map(category -> ServiceProviderApprovalReqResponse.ServiceCategoryInfo.builder()
                        .id(category.getId())
                        .categoryName(category.getCategory().getName()) // Assuming ServiceCategory has getName()
                        .hourlyRate(category.getHourlyRate())
                        .minCharge(category.getMinCharge())
                        .isPrimary(category.getIsPrimary())
                        .createdAt(category.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return ServiceProviderApprovalReqResponse.builder()
                .userId(provider.getUserId()) // Assuming your ServiceProvider has getUserId()
                .firstName(provider.getFirstName())
                .lastName(provider.getLastName())
                .businessName(provider.getBusinessName())
                .description(provider.getDescription())
                .experienceYears(provider.getExperienceYears())
                .isVerified(provider.getIsVerified())
                .verificationDate(provider.getVerificationDate())
                .rating(provider.getRating())
                .totalJobsCompleted(provider.getTotalJobsCompleted())
                .baseServiceCharge(provider.getBaseServiceCharge())
                .phoneSecondary(provider.getPhoneSecondary())
                .address(provider.getAddress())
                .city(provider.getCity())
                .state(provider.getState())
                .pincode(provider.getPincode())
                .availableHoursStart(provider.getAvailableHoursStart())
                .availableHoursEnd(provider.getAvailableHoursEnd())
                .isAvailable(provider.getIsAvailable())
                .createdAt(provider.getCreatedAt())
                .updatedAt(provider.getUpdatedAt())
                .services(services)
                .build();
    }

    public ResponseEntity<ServiceProviderDto> updateApprovalStatus(UUID societyId,UUID serviceProviderId,
                                                                   ApprovalRequest request,
                                                                   String adminEmail) {


        ServiceProviderSociety serviceProviderSociety = serviceProviderSocietyRepository
                .findByServiceProviderUserIdAndSocietyId(serviceProviderId,societyId);


        // Find the admin making the approval/rejection
        Users user = userRepository.findByEmail(adminEmail);
        Admin admin = adminRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        // Update approval status

        serviceProviderSociety.setApprovalStatus(request.getApprovalStatus());
        serviceProviderSociety.setApprovedBy(admin);

        if (request.getApprovalStatus() == ServiceProviderSociety.ApprovalStatus.APPROVED ||
                request.getApprovalStatus() == ServiceProviderSociety.ApprovalStatus.REJECTED) {
            serviceProviderSociety.setApprovedAt(LocalDateTime.now());
        }
        String status;
        if (request.getApprovalStatus() == ServiceProviderSociety.ApprovalStatus.APPROVED) status = "Approved";
        else status = "REJECTED";

        // Save the updated record
        ServiceProviderSociety updatedRecord = serviceProviderSocietyRepository.save(serviceProviderSociety);
        ServiceProvider sp = serviceProviderRepository.findById(updatedRecord.getServiceProvider().getUserId()).orElseThrow(() -> new RuntimeException("Service Provider found"));

        if(status.equals("REJECTED")){
            serviceProviderSocietyRepository.delete(serviceProviderSociety);
        }
        // Return the service provider information
        return ResponseEntity.ok(ServiceProviderDto.status(status, sp.getFirstName(), sp.getLastName(), sp.getBusinessName()));
    }

    public ResponseEntity<List<ServiceProviderProfile>> getPendingApprovalsForSociety(UUID societyId) {
        List<ServiceProviderSociety> pendingSp = serviceProviderSocietyRepository.findBySocietyIdAndApprovalStatus(societyId, ServiceProviderSociety.ApprovalStatus.PENDING);

        List<ServiceProviderProfile> result = new ArrayList<>();

        for (ServiceProviderSociety sps : pendingSp) {
            ServiceProvider serviceProvider = serviceProviderRepository.findById(sps.getServiceProvider().getUserId()).orElseThrow(() -> new RuntimeException("Service Provider found"));
            Users user = userRepository.findById(serviceProvider.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + serviceProvider.getUserId()));

            List<ServiceProviderCategory> spCategories;

            spCategories = serviceProviderCategoryRepository.findByServiceProviderId(serviceProvider.getUserId());


            List<ServiceProviderProfile.ProviderCategory> providerCategories = spCategories.stream()
                    .map(spc -> ServiceProviderProfile.ProviderCategory.builder()
                            .categoryId(spc.getCategory().getId())
                            .categoryName(spc.getCategory().getName())
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
        return ResponseEntity.ok(result);
    }
}