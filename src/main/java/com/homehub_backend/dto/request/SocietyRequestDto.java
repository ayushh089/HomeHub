package com.homehub_backend.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class SocietyRequestDto {
    private String name;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private UUID requestedBy; // Accepts UUID instead of full User object
}
