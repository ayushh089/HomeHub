package com.homehub_backend.controller;


import com.homehub_backend.dto.ServiceProviderDto;
import com.homehub_backend.dto.UserDto;
import com.homehub_backend.dto.request.*;
import com.homehub_backend.dto.response.AuthResponse;
import com.homehub_backend.dto.response.ProfileResponse;
import com.homehub_backend.dto.response.SignupResponse;
import com.homehub_backend.dto.response.VerificationResponse;
import com.homehub_backend.service.AuthService;
import com.homehub_backend.service.JWTService;
import com.homehub_backend.service.UserService;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<VerificationResponse> verifyEmail(@Valid @RequestBody OtpRequest otpRequest,HttpServletResponse response) {
        System.out.println("--------"+otpRequest);
        return authService.verifyEmail(otpRequest,response);
    }



    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) throws AuthException {
        AuthResponse res = authService.login(request,response);
        return ResponseEntity.ok(res);
    }



    @PostMapping("/complete-profile/resident")
    public ResponseEntity<ProfileResponse> completeResidentProfile(
            @CookieValue("jwt") String token,
            @Valid @RequestBody ResidentProfileRequest profileDto) {
        System.out.println("Hey------------------");
        UUID userId = jwtService.extractUserId(token);
        System.out.println(userId);
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

    @PostMapping("/complete-profile/service-provider")
    public ResponseEntity<ProfileResponse> completeServiceProviderProfile(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody ServiceProviderDto profileDto) {
        System.out.println(profileDto);
        UUID userId = jwtService.extractUserId(token);
        System.out.println("hey");
        return authService.completeServiceProviderProfile(userId, profileDto);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@CookieValue("jwt") String token) {
        UUID userId = jwtService.extractUserId(token);
        UserDto dto=authService.findMe(userId);

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // Clear JWT
        System.out.println("logging out");
        Cookie accessTokenCookie = new Cookie("jwt", "");
        System.out.println(accessTokenCookie);// "jwt" is your cookie name
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true); // For HTTPS
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(0); // Immediately expire
        response.addCookie(accessTokenCookie);

        return ResponseEntity.ok().build();
    }

    private void clearAuthenticationCookies(HttpServletRequest request, HttpServletResponse response) {
        // If you're using JWT in cookies, clear them
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt") ) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0); // Immediately expire
                    response.addCookie(cookie);
                }
            }
        }
    }

}
