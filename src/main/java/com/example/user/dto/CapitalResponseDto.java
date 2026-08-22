package com.example.user.dto;

import com.example.user.entity.Country;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CapitalResponseDto {
    private Long id;
    private String name;
}
