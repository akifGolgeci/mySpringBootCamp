package com.example.driver.service;

import com.example.driver.dto.CarRequestDto;
import com.example.driver.dto.CarResponseDto;
import com.example.driver.exception.CarAlreadyExistException;
import com.example.driver.exception.CarNotFoundException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.sql.Driver;
import java.util.List;

@Service
public class DriverServiceWebClientSync {
    private final WebClient webClient;

    public DriverServiceWebClientSync(WebClient webClient) {
        this.webClient = webClient;
    }

    // WebClient 'ta liste döndürme yöntemleri
    public List<CarResponseDto> getAll() {
        return webClient.get()
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<CarResponseDto>>(){})
                .block();
    }

    public List<CarResponseDto> getAll2() {
        return webClient.get()
                .retrieve()
                .bodyToFlux(CarResponseDto.class)
                .collectList()
                .block();
    }

    public ResponseEntity<List<CarResponseDto>> getAll3() {
        return webClient.get()
                .retrieve()
                .toEntityList(CarResponseDto.class)
                .block();
    }

    public ResponseEntity<List<CarResponseDto>> getAll4() {
        return webClient.get()
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<CarResponseDto>>(){})
                .block();
    }


    public ResponseEntity<CarResponseDto> getCarById(Integer id) {
        return webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new CarNotFoundException("Car not found with id " + id)))
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new RuntimeException("Server Error")))
                .toEntity(CarResponseDto.class)
                .block();
    }

    public CarResponseDto createCar(CarRequestDto carRequestDto) {
        return webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(carRequestDto)
                .retrieve()
                .onStatus(status -> status.value() == HttpStatus.BAD_REQUEST.value(),
                        clientResponse -> Mono.error(new CarAlreadyExistException("car already exists ")))
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new RuntimeException("Server Error")))
                .bodyToMono(CarResponseDto.class)
                .block();
    }

    public ResponseEntity<CarResponseDto> updateCar(Integer id, CarRequestDto carRequestDto) {
        return webClient.put()
                .uri("/id/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(carRequestDto)
                .retrieve()
                .onStatus(status -> status.value() == HttpStatus.NOT_FOUND.value(),
                        clientResponse -> Mono.error(new CarNotFoundException("car not found with id: " + id)))
                .onStatus(status -> status.value() == HttpStatus.BAD_REQUEST.value(),
                        clientResponse -> Mono.error(new CarAlreadyExistException("car already exists ")))
                .onStatus(status -> status.is5xxServerError(),
                        clientResponse -> Mono.error(new RuntimeException("Server Error")))
                .toEntity(CarResponseDto.class)
                .block();
    }

    public ResponseEntity<Void> deleteCar(Integer id) {
        return webClient.delete()
                .uri("/id/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new CarNotFoundException("Car not found with id " + id)))
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new RuntimeException("Server Error")))
                .toBodilessEntity()
                .block();
    }

    public CarResponseDto getCarByModelRequestParam(String model) {
        return webClient.get()
                .uri("/model?model={model}", model)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new CarNotFoundException("Car not found with model: " + model)))
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new RuntimeException("Server Error")))
                .bodyToMono(CarResponseDto.class)
                .block();
    }
}







