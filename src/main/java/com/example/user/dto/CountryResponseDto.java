package com.example.user.dto;

import com.example.user.entity.Capital;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryResponseDto {
    private Long id;
    private String name;
    private Integer population;
    private String capitalName;
    private Long capitalId;
}
