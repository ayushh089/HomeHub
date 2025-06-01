package com.homehub_backend.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class ServiceCategoryRequest {
    private String name;
    private String description;
}
