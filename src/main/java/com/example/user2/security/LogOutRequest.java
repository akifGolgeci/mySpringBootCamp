package com.example.user2.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Dto classı
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogOutRequest {

    private String accessToken;
}
