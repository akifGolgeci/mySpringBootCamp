package com.example.driver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarResponseDto {

    private Integer id;

    private String brand;

    private String model;

    private Integer price;

    private Integer year;
}
