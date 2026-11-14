package com.example.user2.security;

import com.example.user2.dto.OldUserRegisterDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OldRegisterController {

    @PostMapping("/passwordMatches")
    public ResponseEntity<String> passwordMatches(@Valid @RequestBody OldUserRegisterDto oldUserRegisterDto) {
        return ResponseEntity.ok("Password Matches");
    }

}
