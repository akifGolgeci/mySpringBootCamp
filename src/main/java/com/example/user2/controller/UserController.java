package com.example.user2.controller;


import com.example.user2.dto.UserRequestDto;
import com.example.user2.dto.UserResponseDto;
import com.example.user2.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users2")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserResponseDto> getUser(@RequestParam long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<UserResponseDto> createUser(@PathVariable long id, @Valid @RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(id, userRequestDto));
    }




}
