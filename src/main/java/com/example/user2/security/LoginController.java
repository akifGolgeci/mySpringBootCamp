package com.example.user2.security;

import com.example.user2.service.LoginService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/users2/LoginController")
@Controller
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    // MyAuthenticationProviderda authentication kontrolü yaptığımız için bu login metoduna gerek kalmadı
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto) {
        return loginService.login(loginDto);
    }

    // MyAuthenticationProvider ile yapılmış login
    @PostMapping("/login2")
    public ResponseEntity<?> login2(@Valid @RequestBody LoginDto loginDto) {
        return loginService.login2(loginDto);
    }
}
