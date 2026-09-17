package com.example.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
// Capital ların DB 'de var olduğunu kabul ederek yazıyoruz.
public class CountryRequestDto {
    private String name;
    private Integer population;
    private Long capitalId;

}
