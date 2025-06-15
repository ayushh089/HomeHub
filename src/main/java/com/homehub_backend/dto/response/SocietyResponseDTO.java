package com.homehub_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocietyResponseDTO {
   public UUID id;
   public String name;
   public String address;
   public String city;
   public String state;
   public String pincode;
   public String status;// "NOT_APPLIED", "PENDING", "APPROVED", "REJECTED"
}