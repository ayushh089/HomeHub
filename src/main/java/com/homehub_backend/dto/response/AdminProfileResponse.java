package com.homehub_backend.dto.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class AdminProfileResponse {

    private String name;
    private String email;
    private String phone;

}
