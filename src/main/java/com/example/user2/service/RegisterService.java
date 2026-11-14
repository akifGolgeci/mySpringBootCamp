package com.example.user2.service;

import com.example.user2.security.MyUserDetailsService;
import com.example.user2.dto.RegisterDto;
import com.example.user2.dto.UserResponseDto;
import com.example.user2.entity.RolesEnum;
import com.example.user2.entity.User;
import com.example.user2.mapper.UserMapper;
import com.example.user2.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RegisterService {

    private final MyUserDetailsService myUserDetailsService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public RegisterService(MyUserDetailsService myUserDetailsService, UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.myUserDetailsService = myUserDetailsService;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<?> register(RegisterDto registerDto) {
        try {
            UserDetails userDetails = myUserDetailsService.loadUserByUsername(registerDto.getUsername());

            return ResponseEntity.status(HttpStatus.CONFLICT).body("User is already registered");
        }
        catch (UsernameNotFoundException e) {
            // kullanıcı yoksa register işlemleri yapılır.
            User user = new User();
            user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
            user.setUsername(registerDto.getUsername());
            user.setRoles(RolesEnum.ROLE_USER);

            User savedUser = userRepository.save(user);
            UserResponseDto userResponseDto = userMapper.userToUserResponse(savedUser);

            return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
        }
        catch (Exception e) {
            String message = e.getMessage();
            String simpleName = e.getClass().getSimpleName();
            log.info("message: {} ", message);
            log.info("className: {} ", simpleName);
            throw new RuntimeException(e.getMessage());
        }

    }
}
