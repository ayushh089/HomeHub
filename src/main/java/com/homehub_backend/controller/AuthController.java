package com.homehub_backend.controller;


import com.homehub_backend.dto.request.LoginRequest;
import com.homehub_backend.dto.request.OtpRequest;
import com.homehub_backend.dto.request.SignUpRequest;
import com.homehub_backend.dto.response.SignupResponse;
import com.homehub_backend.dto.response.VerificationResponse;
import com.homehub_backend.service.AuthService;
import com.homehub_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    UserService userService;

    @Autowired
    AuthService authService;

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
}
