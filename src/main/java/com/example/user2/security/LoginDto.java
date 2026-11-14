package com.example.user2.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDto {
    @NotBlank(message = "username cant be blank")
    @Size(min = 3, max = 10, message = "Length should be between 3 and 10")
    private String username;

    @NotNull(message = "password cant be null")
    @Size(min = 3, message = "password length should be at least 3")
    private String password;
}
