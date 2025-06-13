package com.homehub_backend.dto.response;


import com.homehub_backend.entity.Society;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocietyDataResponse {

    private UUID id;
    private String name;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private String approvalStatus;
    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;
    private String requestedByUserName;
    private String requestedByEmail;
    private String requestedByPhone;// Optionally expose requester info
//    private String approvedByAdminName;  // Optionally expose approver info

}
