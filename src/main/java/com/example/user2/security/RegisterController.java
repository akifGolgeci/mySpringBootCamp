package com.example.user2.security;

import com.example.user2.dto.RegisterDto;
import com.example.user2.service.RegisterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users2/register")
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    // @PreAuthorize ile Custom metod da çağırabiliriz.
    // @PreAuthorize("#myUserDetailsService.loadUserByUsername(registerDto.username)")
    @PostMapping
    public ResponseEntity<?>  register(@RequestBody RegisterDto registerDto) {
        return registerService.register(registerDto);
    }
}
