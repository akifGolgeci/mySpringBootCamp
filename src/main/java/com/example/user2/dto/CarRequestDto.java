package com.example.user2.dto;

import com.example.user2.customAnnatation.EvenNumber;
import com.example.user2.customAnnatation.NotAdmin;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

// log çıktısında dizi gibi şeyler varsa @ToStringle daha okunabilir olmasını sağlıyoruz.
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarRequestDto {

    // "", " " ve null' ı kabul etmez
    @NotBlank(message = "brand cant be blank")

    //@Size STRİNG COLLECTİON MAPLERDE KUllanılabilir.
    @Size(min = 2,max = 10,message = "Brand size should be between 2-10")

    //Custom validationım
    @NotAdmin
    private String brand;

    @NotBlank(message = "model cant be blank")

    //@Length sadece stringler içindir @Size ile benzerdir.
    @Length(min = 1, max = 5, message = "model length should be between 1-5")
    private String model;

    @NotNull(message = "price cant be null")
    @PositiveOrZero(message = "price cant be negative")
    @Max(value = 99999, message = "price cant be higher than 99999")

    //Custom validationım
    @EvenNumber(message = "Price should be even number")
    private Integer price;

    @NotNull(message = "year cant be null")
    @PositiveOrZero(message = "year cant be negative")
    @Max(value = 30,message = "Year cant be higher than 30")

    //Custom validationım
    @EvenNumber(message = "Year should be even number")
    private Integer year;

//    // "" ve null' ı kabul etmez.
//    @NotEmpty(message = "Owners cant be empty")
//    private List<String> owners;
//
//    @Past(message = "created date should be in the past ")
//    private LocalDateTime createdDate;
//
//    @Future(message = "vehicle inspection should be in the future")
//    private LocalDateTime inspectionDate;
//
//    @Email(message = "invalid email format")
//    @NotBlank(message = "email cant be blank")
//    private String email;

}
