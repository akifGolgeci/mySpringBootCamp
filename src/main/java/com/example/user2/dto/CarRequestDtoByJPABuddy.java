package com.example.user2.dto;

import jakarta.validation.constraints.*;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * DTO for {@link com.example.user2.entity.Car}
 */
@Value
public class CarRequestDtoByJPABuddy implements Serializable {

    @Length(message = "Brand length should be between 3-30", min = 3, max = 30)
    @NotBlank(message = "Brand can't be blank")
    String brand;

    @NotBlank
    @Length(message = "Model length should be between 1-10", min = 1, max = 10)
    String model;

    @NotNull(message = "Price cant be null")
    @Min(message = "Price should be higher than 10000", value = 10000)
    @Max(message = "Price should be lower than 99999", value = 99999)
    @Positive(message = "Price should be positive")
    Integer price;

    @NotNull(message = "Year can't be null")
    @Min(message = "Year cant be negative", value = 0)
    @Max(30)
    Integer year;
}