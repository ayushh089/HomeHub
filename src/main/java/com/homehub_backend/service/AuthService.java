package com.homehub_backend.service;

import com.homehub_backend.dao.UserRepository;

import com.homehub_backend.dto.UserDto;
import com.homehub_backend.dto.UserRole;
import com.homehub_backend.dto.request.*;
import com.homehub_backend.dto.response.ProfileResponse;
import com.homehub_backend.dto.response.SignupResponse;
import com.homehub_backend.dto.response.VerificationResponse;
import com.homehub_backend.entity.Users;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
public class AuthService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    UserService userService;

    @Autowired
    OtpService otpService;

    @Autowired
    JWTService jwtService;

    @Autowired
    ResidentService residentService;

    @Autowired
    AdminService adminService;

    @Autowired
    PlatformAdminService platformAdminService;

    private String generateVerificationCode() {
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }

    public ResponseEntity<SignupResponse> createInitialUser(SignUpRequest signupDto) {
        System.out.println(signupDto);
        if (userRepository.findByEmail(signupDto.getEmail()) != null) {
            return ResponseEntity.badRequest()
                    .body(SignupResponse.error("Email already exists"));
        }

        if (userRepository.findByPhone(signupDto.getPhone()) != null) {
            return ResponseEntity.badRequest()
                    .body(SignupResponse.error("Phone number already exists"));
        }


        String verificationCode = generateVerificationCode();
        UserDto userdto = UserDto.builder()
                .email(signupDto.getEmail())
                .password(signupDto.getPassword())
                .phone(signupDto.getPhone())
                .role(signupDto.getRole().toString())
                .build();

        Users user = userService.addUser(userdto);

        OtpRequest otpRequest = OtpRequest.builder()
                .userId(user.getId())
                .otpCode(verificationCode)
                .build();
        otpService.addOtp(otpRequest);

        System.out.println(userdto);
        emailService.sendVerificationEmail(user.getEmail(), verificationCode);

        return ResponseEntity.ok(SignupResponse.success(user.getId().toString(), signupDto.getRole()));
    }


    public ResponseEntity<VerificationResponse> verifyEmail(OtpRequest otpRequest) {
        System.out.println("For Verification "+otpRequest);
        UUID userId = otpRequest.getUserId();
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify code (implement your verification logic)
        String codeVerificationStatus=otpService.verifyOtp(otpRequest);
        if (!Objects.equals(codeVerificationStatus, "OK")) {
            return ResponseEntity.badRequest()
                    .body(VerificationResponse.error(codeVerificationStatus));
        }

        user.setEmailVerified(true);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);


        String token = jwtService.generateToken(user.getEmail(), user.getRole(),user.getId());

        return ResponseEntity.ok(VerificationResponse.success(token, UserRole.valueOf(user.getRole())));
    }

    public ResponseEntity<ProfileResponse> completeResidentProfile(UUID userId, @Valid ResidentProfileRequest profileDto) {
        return residentService.createResident(userId, profileDto);
    }

    public ResponseEntity<ProfileResponse> completeSocietyAdminProfile(UUID userId, AdminProfileRequest profileDto) {
        return adminService.createAdmin(userId,profileDto);
    }

    public ResponseEntity<ProfileResponse> completePlatformAdminProfile(UUID userId, @Valid PlatformAdminProfileRequest profileDto) {
        return platformAdminService.createPlatformAdmin(userId,profileDto);
    }
}
