package com.homehub_backend.dto.response;


import lombok.Data;

import java.util.UUID;

@Data
public class ServiceCategoryResponse {
    private  UUID id;
    private String name;
    private String description;

}
