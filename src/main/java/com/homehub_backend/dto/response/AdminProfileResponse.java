package com.homehub_backend.dto.response;


import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder

public class AdminProfileResponse {

    private String name;
    private String email;
    private UUID societyId;
    private String phone;


}
