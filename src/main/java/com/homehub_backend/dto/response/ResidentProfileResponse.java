package com.homehub_backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ResidentProfileResponse {

    UUID userId;
    String firstName;
    String lastName;
    String apartmentNumber;
    String societyName;
    UUID societyId;
    String societyAddress;
    String email;
    String phone;


}
