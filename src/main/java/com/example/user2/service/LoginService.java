package com.example.user2.service;

import com.example.user2.security.JWTService;
import com.example.user2.security.MyAuthenticationProvider;
import com.example.user2.security.MyUserDetailsService;
import com.example.user2.security.LoginDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;

@Service
// Authentication kendisi interface bu yüzden onu implemente eden UsernamePasswordAuthenticationToken authentication ile
// nesnesi oluşturabiliriz
public class LoginService {

    private final MyUserDetailsService myUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final MyAuthenticationProvider myAuthenticationProvider;
    private final JWTService jWTService;

    public LoginService(MyUserDetailsService myUserDetailsService, PasswordEncoder passwordEncoder, MyAuthenticationProvider myAuthenticationProvider, JWTService jWTService) {
        this.myUserDetailsService = myUserDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.myAuthenticationProvider = myAuthenticationProvider;
        this.jWTService = jWTService;
    }

    public ResponseEntity<?> login(LoginDto loginDto) {
        try {
            //Username bakıldı
            UserDetails userDetails = myUserDetailsService.loadUserByUsername(loginDto.getUsername());

            //Şifreye bakıldı
            boolean matches = passwordEncoder.matches(loginDto.getPassword(), userDetails.getPassword());

            if (matches) {
                String token = jWTService.generateToken(userDetails);

                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
                return ResponseEntity.ok(Map.of(
                        "response", "successed",
                        "path", request.getRequestURI(),
                        "token", token,
                        "status code", HttpStatus.OK.value()
                ));
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Incorrect password");
        }
        catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    public ResponseEntity<?> login2(@Valid LoginDto loginDto) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        return ResponseEntity.ok(myAuthenticationProvider.authenticate(authentication));
    }
}
