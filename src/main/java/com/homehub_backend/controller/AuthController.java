package com.homehub_backend.controller;


import com.homehub_backend.dto.request.LoginRequest;
import com.homehub_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest req){
        System.out.println("----------------------------------"+req);
        return userService.authenticate(req);
    }
}
