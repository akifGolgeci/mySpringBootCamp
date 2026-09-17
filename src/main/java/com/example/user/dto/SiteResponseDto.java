package com.example.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SiteResponseDto {
    private Long id;
    private String name;
    private String port;
    private UserResponse user;
}
