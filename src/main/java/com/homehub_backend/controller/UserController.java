package com.homehub_backend.controller;


import com.homehub_backend.dto.UserDto;
import com.homehub_backend.entity.Users;
import com.homehub_backend.service.UserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {


    @Autowired
    UserService userService;

    @PostMapping
    public Users addUser(@RequestBody UserDto user) {
        return userService.addUser(user);


    }
}
