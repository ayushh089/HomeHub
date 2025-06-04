package com.homehub_backend.service;

import com.homehub_backend.dao.UserRepository;
import com.homehub_backend.dto.UserDto;
import com.homehub_backend.dto.request.LoginRequest;
import com.homehub_backend.entity.Users;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(12);

    public Users addUser(UserDto dto) {
        dto.setPassword(encoder.encode(dto.getPassword()));
        Users user = Users.builder()
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .passwordHash(dto.getPassword())
                .role(dto.getRole())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Users savedUser = userRepository.save(user);


        return savedUser;
    }

    public Users updateUser(Users userToUpdate) {
        Optional<Users> existingUserOpt = userRepository.findById(userToUpdate.getId());
        if (existingUserOpt.isEmpty()) {
            throw new IllegalArgumentException("User with id " + userToUpdate.getId() + " not found");
        }

        Users existingUser = existingUserOpt.get();

        // Update mutable fields
        existingUser.setEmail(userToUpdate.getEmail());
        existingUser.setPhone(userToUpdate.getPhone());
        existingUser.setRole(userToUpdate.getRole());

        // Only update passwordHash if non-null and non-empty to avoid overwriting unintentionally
        if (userToUpdate.getPasswordHash() != null && !userToUpdate.getPasswordHash().isEmpty()) {
            existingUser.setPasswordHash(userToUpdate.getPasswordHash());
        }

        existingUser.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(existingUser);
    }

    public String authenticate(LoginRequest req) {
        Authentication authentication=
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(),req.getPassword()));

        if(authentication.isAuthenticated()){
            Users user = userRepository.findByEmail(req.getEmail());
            return jwtService.generateToken(req.getEmail(),user.getRole(),user.getId());
        }
        return "LLL";
    }
}
