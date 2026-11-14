package com.example.user2.security;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//Dto classı
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequest {

    private String token;


}
