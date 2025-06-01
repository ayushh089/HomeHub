package com.homehub_backend.service;

import com.homehub_backend.dao.ServiceCategoryRepository;
import com.homehub_backend.dao.ServiceProviderCategoryRepository;
import com.homehub_backend.dao.ServiceProviderRepository;
import com.homehub_backend.dto.request.ServiceProviderCategoryRequest;
import com.homehub_backend.dto.response.ServiceProviderCategoryResponse;
import com.homehub_backend.entity.ServiceCategory;
import com.homehub_backend.entity.ServiceProvider;
import com.homehub_backend.entity.ServiceProviderCategory;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ServiceProviderCategoryService {
    @Autowired
    private final ServiceProviderCategoryRepository serviceProviderCategoryRepository;
    @Autowired
    private final ServiceProviderRepository serviceProviderRepository;
    @Autowired
    private final ServiceCategoryRepository serviceCategoryRepository;

    private final ModelMapper modelMapper;

    public ResponseEntity<ServiceProviderCategoryResponse> addServiceProviderCategory(ServiceProviderCategoryRequest dto) {
        // Fetch related entities
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
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
