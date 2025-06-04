package com.homehub_backend.controller;


import com.homehub_backend.dto.request.*;
import com.homehub_backend.dto.response.ProfileResponse;
import com.homehub_backend.dto.response.SignupResponse;
import com.homehub_backend.dto.response.VerificationResponse;
import com.homehub_backend.service.AuthService;
import com.homehub_backend.service.JWTService;
import com.homehub_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    UserService userService;

    @Autowired
    AuthService authService;

    @Autowired
    JWTService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> initialSignup(@Valid @RequestBody SignUpRequest signupDto) {
        return authService.createInitialUser(signupDto);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<VerificationResponse> verifyEmail(@Valid @RequestBody OtpRequest otpRequest) {
        System.out.println("--------"+otpRequest);
        return authService.verifyEmail(otpRequest);
    }

    //! login------------------------------
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest req){
        System.out.println("----------------------------------"+req);
        return userService.authenticate(req);
    }



    @PostMapping("/complete-profile/resident")
    public ResponseEntity<ProfileResponse> completeResidentProfile(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody ResidentProfileRequest profileDto) {
        System.out.println("Hey------------------");
        UUID userId = jwtService.extractUserId(token);
        return authService.completeResidentProfile(userId, profileDto);
    }

    @PostMapping("/complete-profile/society-admin")
    public ResponseEntity<ProfileResponse> completeSocietyAdminProfile(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody AdminProfileRequest profileDto) {
        UUID userId = jwtService.extractUserId(token);
        return authService.completeSocietyAdminProfile(userId, profileDto);
    }
    @PostMapping("/complete-profile/platform-admin")
    public ResponseEntity<ProfileResponse> completePlatformAdminProfile(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody PlatformAdminProfileRequest profileDto) {
        UUID userId = jwtService.extractUserId(token);
        return authService.completePlatformAdminProfile(userId, profileDto);
    }

}
