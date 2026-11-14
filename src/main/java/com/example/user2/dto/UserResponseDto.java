package com.example.user2.dto;

import com.example.user2.entity.RolesEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Long id;
    private String username;
  //private String password;
    private Integer age;
    private Integer salary;
    private RolesEnum roles;
}
