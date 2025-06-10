package com.homehub_backend.service;

import com.homehub_backend.dao.ServiceCategoryRepository;
import com.homehub_backend.dao.ServiceProviderCategoryRepository;
import com.homehub_backend.dao.ServiceProviderRepository;
import com.homehub_backend.dao.UserRepository;
import com.homehub_backend.dto.request.ServiceProviderCategoryRequest;
import com.homehub_backend.dto.response.ServiceProviderCategoryResponse;
import com.homehub_backend.entity.ServiceCategory;
import com.homehub_backend.entity.ServiceProvider;
import com.homehub_backend.entity.ServiceProviderCategory;
import com.homehub_backend.entity.Users;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceProviderCategoryService {
    @Autowired
    private final ServiceProviderCategoryRepository serviceProviderCategoryRepository;
    @Autowired
    private final ServiceProviderRepository serviceProviderRepository;
    @Autowired
    private final ServiceCategoryRepository serviceCategoryRepository;

    @Autowired
    UserRepository userRepository;

    private final ModelMapper modelMapper;

    public ResponseEntity<ServiceProviderCategoryResponse> addServiceProviderCategory(ServiceProviderCategoryRequest dto) {

        ServiceProvider serviceProvider = serviceProviderRepository.findById(dto.getServiceProviderId())
                .orElseThrow(() -> new RuntimeException("Service provider not found"));

        ServiceCategory category = serviceCategoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        ServiceProviderCategory spCategory = ServiceProviderCategory.builder()
                .hourlyRate(dto.getHourlyRate())
                .minCharge(dto.getMinCharge())
                .isPrimary(dto.getIsPrimary())
                .createdAt(LocalDateTime.now())
                .serviceProvider(serviceProvider)

                .category(category)
                .build();

        // Save
        ServiceProviderCategory saved = serviceProviderCategoryRepository.save(spCategory);

        // Prepare response
        ServiceProviderCategoryResponse response = ServiceProviderCategoryResponse.builder()
                .id(saved.getId())
                .hourlyRate(saved.getHourlyRate())
                .minCharge(saved.getMinCharge())
                .isPrimary(saved.getIsPrimary())
                .serviceProviderId(serviceProvider.getUserId())
                .categoryId(category.getId())
                .createdAt(saved.getCreatedAt())
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public List<ServiceProviderCategoryResponse> getServiceProviderCategories(UUID serviceProviderId) {
        System.out.println("Looking up categories for provider ID: " + serviceProviderId);


        Users user = userRepository.findById(serviceProviderId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + serviceProviderId));

        System.out.println(user);

        List<ServiceProviderCategory> categories = serviceProviderCategoryRepository
                .findByServiceProviderId(serviceProviderId);
        System.out.println(categories);

        return categories.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    private ServiceProviderCategoryResponse mapToResponse(ServiceProviderCategory category) {
        return ServiceProviderCategoryResponse.builder()
                .id(category.getId())
                .hourlyRate(category.getHourlyRate())
                .minCharge(category.getMinCharge())
                .isPrimary(category.getIsPrimary())
                .createdAt(category.getCreatedAt())
                .serviceProviderId(category.getServiceProvider().getUserId())
                .categoryId(category.getCategory().getId())
                .build();
    }


    public ServiceProviderCategoryResponse updateServiceProviderCategory(UUID id, ServiceProviderCategoryRequest request) {
        ServiceProviderCategory existing = serviceProviderCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service provider category not found with id: " + id));
        System.out.println("hy");
        // If setting as primary, unset other primary categories for this provider
        if (Boolean.TRUE.equals(request.getIsPrimary()) && !existing.getIsPrimary()) {
            serviceProviderCategoryRepository.updatePrimaryStatusForProvider(
                    existing.getServiceProvider().getUserId(), false);
        }

        existing.setHourlyRate(request.getHourlyRate());
        existing.setMinCharge(request.getMinCharge());
        existing.setIsPrimary(request.getIsPrimary());

        ServiceProviderCategory updated = serviceProviderCategoryRepository.save(existing);
        return mapToResponse(updated);
    }
}
