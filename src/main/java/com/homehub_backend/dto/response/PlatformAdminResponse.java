package com.homehub_backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlatformAdminResponse {

    private String email;
    private String phone;
    private String firstName;
    private String lastName;
}
