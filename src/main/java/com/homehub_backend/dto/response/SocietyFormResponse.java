package com.homehub_backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SocietyFormResponse {
    private UUID societyId;
    private String name;
    private String address;
    private String city;
    private String pincode;
    private UUID requestedBy;
    private String email;
    private String number;

}
