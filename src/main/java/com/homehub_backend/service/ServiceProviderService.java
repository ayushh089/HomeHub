package com.homehub_backend.service;

import com.homehub_backend.dao.ServiceProviderRepository;
import com.homehub_backend.dto.ServiceProviderDto;
import com.homehub_backend.dto.UserDto;
import com.homehub_backend.entity.Resident;
import com.homehub_backend.entity.ServiceProvider;
import com.homehub_backend.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Service
public class ServiceProviderService {

    @Autowired
    ServiceProviderRepository serviceProviderRepository;

    @Autowired
    UserService userService;
    public ResponseEntity<ServiceProviderDto> createServiceProvider(ServiceProviderDto dto) {
        UserDto userDto = new UserDto();
        userDto.setEmail(dto.getEmail());
        userDto.setPhone(dto.getPhone());
        userDto.setPassword(dto.getPassword());
        userDto.setRole(dto.getRole());
        Users savedUser=userService.addUser(userDto);

        ServiceProvider newProvider = ServiceProvider.builder()
                .user(savedUser)
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .businessName(dto.getBusinessName())
                .description(dto.getDescription())
                .experienceYears(dto.getExperienceYears() != null ? dto.getExperienceYears() : 0)
                .isVerified(dto.getIsVerified() != null ? dto.getIsVerified() : false)
                .verificationDate(dto.getVerificationDate())
                .rating(dto.getRating() != null ? dto.getRating() : BigDecimal.ZERO)
                .totalJobsCompleted(dto.getTotalJobsCompleted() != null ? dto.getTotalJobsCompleted() : 0)
                .baseServiceCharge(dto.getBaseServiceCharge())
                .phoneSecondary(dto.getPhoneSecondary())
                .address(dto.getAddress())
                .city(dto.getCity())
                .state(dto.getState())
                .pincode(dto.getPincode())
                .availableHoursStart(dto.getAvailableHoursStart())
                .availableHoursEnd(dto.getAvailableHoursEnd())
                .isAvailable(dto.getIsAvailable() != null ? dto.getIsAvailable() : true)
                .createdAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDateTime.now())
                .updatedAt(dto.getUpdatedAt() != null ? dto.getUpdatedAt() : LocalDateTime.now())
                .build();


        ServiceProvider savedProvider = serviceProviderRepository.save(newProvider);
        System.out.println("hy");

        return new ResponseEntity<>(dto, HttpStatus.CREATED);


    }
}
