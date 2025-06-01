package com.homehub_backend.service;

import com.homehub_backend.dao.ServiceCategoryRepository;
import com.homehub_backend.dto.request.ServiceCategoryRequest;
import com.homehub_backend.dto.response.ServiceCategoryResponse;
import com.homehub_backend.entity.ServiceCategory;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServiceCategoryService {

    @Autowired
    ServiceCategoryRepository serviceCategoryRepository;

    private final ModelMapper modelMapper;

    public ResponseEntity<ServiceCategoryResponse> addServiceCategory(ServiceCategoryRequest dto) {
        System.out.println("Received: " + dto); // or use logger

        ServiceCategory newCategory = modelMapper.map(dto, ServiceCategory.class);
        newCategory.setCreatedAt(LocalDate.now().atStartOfDay());

//        System.out.println("Mapped description: " + newCategory.getDescription());
        ServiceCategory savedCategory = serviceCategoryRepository.save(newCategory);
        ServiceCategoryResponse response = modelMapper.map(savedCategory, ServiceCategoryResponse.class);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public List<ServiceCategoryResponse> getAllServiceCategories() {
        List<ServiceCategory> categories = serviceCategoryRepository.findAll();
        return categories.stream()
                .map(cat -> modelMapper.map(cat, ServiceCategoryResponse.class))
                .toList();
    }

    public ServiceCategoryResponse getServiceCategoryById(UUID id) {
        ServiceCategory category = serviceCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service category not found"));
        return modelMapper.map(category, ServiceCategoryResponse.class);
    }

    public ServiceCategoryResponse updateServiceCategory(UUID id, ServiceCategoryRequest dto) {
        ServiceCategory existing = serviceCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service category not found"));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        // Optionally update other fields
        ServiceCategory updated = serviceCategoryRepository.save(existing);
        return modelMapper.map(updated, ServiceCategoryResponse.class);
    }

    public void deleteServiceCategory(UUID id) {
        if (!serviceCategoryRepository.existsById(id)) {
            throw new RuntimeException("Service category not found");
        }
        serviceCategoryRepository.deleteById(id);
    }


}
