package com.example.user2.dto;

import com.example.user2.customAnnatation.PasswordMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@PasswordMatches
public class OldUserRegisterDto {

    @NotBlank(message = "username cant be empty")
    private String username;

    @NotBlank(message = "password cant be empty")
    private String password;

    @NotBlank(message = "confirm password cant be empty")
    private String confirmPassword;

    @NotBlank(message = "email cant be empty")
    @Email
    private String email;
}
