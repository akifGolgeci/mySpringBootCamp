package com.example.user2.controller;

import com.example.user2.dto.UserRegisterDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

    @PostMapping("/passwordMatches")
    public ResponseEntity<String> passwordMatches(@Valid @RequestBody UserRegisterDto userRegisterDto) {
        return ResponseEntity.ok("Password Matches");
    }

}
