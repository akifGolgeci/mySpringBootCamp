package com.example.user2.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// dto classı
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenPair {
    private String accessToken;
    private String refreshToken;
}
