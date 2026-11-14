package com.example.user2.controller;

import com.example.user2.dto.CarRequestDto;
import com.example.user2.dto.CarResponseDto;
import com.example.user2.entity.Car;

import com.example.user2.security.JWTFilter;
import com.example.user2.security.MyUserDetails;
import com.example.user2.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Spring security otomatik olarak JWT filter gibi güvenlik bileşenlerini yüklediği için proje hata veriyor.
// Bu yüzden spring securityi eklemeden testleri yaparsak hata almayız.
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = CarController.class, excludeAutoConfiguration =  { SecurityAutoConfiguration.class })
public class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    @MockBean
    private JWTFilter jwtFilter;

    @MockBean
    private MyUserDetails  myUserDetails;

    private Car car;
    private CarResponseDto carResponseDto;
    private CarRequestDto carRequestDto;

    @BeforeEach
    public void setup() {
        carRequestDto = new CarRequestDto("Mercedes", "a0", 14, 10);
        car = new Car(1,"Mercedes", "a0", 14, 10);
        carResponseDto = new CarResponseDto(1,"ferrari", "b2", 66, 4);
    }

    @Test
    void getCarById_ShouldReturnCar() throws Exception {

        when(carService.getCarById(1)).thenReturn(carResponseDto);

        mockMvc.perform(get("/api/v1/cars2/{id}",1)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )       .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.brand").value("ferrari"))
                .andExpect(jsonPath("$.model").value("b2"))
                .andExpect(jsonPath("$.price").value(66))
                .andExpect(jsonPath("$.year").value(4));

    }

}
