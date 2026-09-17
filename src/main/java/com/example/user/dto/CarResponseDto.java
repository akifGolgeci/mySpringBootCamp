package com.example.user.dto;

import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO for {@link com.example.user.entity.Car}
 */
@Value
public class CarResponseDto implements Serializable {
    Integer id;
    @Size(max = 35)
    String brand;
    Integer year;
    @Size(max = 35)
    String color;
    BigDecimal price;
    Boolean otomatic;
    Instant createdDate;
}