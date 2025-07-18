package com.homehub_backend.controller;

import com.homehub_backend.dto.request.ServiceCategoryRequest;
import com.homehub_backend.dto.response.ServiceCategoryResponse;
import com.homehub_backend.service.ServiceCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.SecureCacheResponse;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/serviceCategory")
//@PreAuthorize("hasRole('PLATFORM_ADMIN')")
public class ServiceCategoryController {

    @Autowired
    ServiceCategoryService serviceCategoryService;

    @PostMapping

    public ResponseEntity<ServiceCategoryResponse> addServiceCategory(@RequestBody ServiceCategoryRequest dto){
            return serviceCategoryService.addServiceCategory(dto);
    }
    @GetMapping
    public ResponseEntity<List<ServiceCategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(serviceCategoryService.getAllServiceCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceCategoryResponse> getCategoryById(@PathVariable UUID id) {
        return ResponseEntity.ok(serviceCategoryService.getServiceCategoryById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceCategoryResponse> updateCategory(@PathVariable UUID id, @RequestBody ServiceCategoryRequest dto) {
        return ResponseEntity.ok(serviceCategoryService.updateServiceCategory(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        serviceCategoryService.deleteServiceCategory(id);
        return ResponseEntity.noContent().build();
    }
}
