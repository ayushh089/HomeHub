package com.homehub_backend.controller;


import com.homehub_backend.dto.request.ServiceCategoryRequest;
import com.homehub_backend.dto.request.ServiceProviderCategoryRequest;
import com.homehub_backend.dto.response.ServiceCategoryResponse;
import com.homehub_backend.dto.response.ServiceProviderCategoryResponse;
import com.homehub_backend.service.ServiceProviderCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/serviceProviderCategory")
public class ServiceProviderCategoryController {

    @Autowired
    ServiceProviderCategoryService serviceProviderCategoryService;

    @PostMapping
    public ResponseEntity<ServiceProviderCategoryResponse> addServiceProviderCategory(@RequestBody ServiceProviderCategoryRequest dto){
        return serviceProviderCategoryService.addServiceProviderCategory(dto);
    }
}
