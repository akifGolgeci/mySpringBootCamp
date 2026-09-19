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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class DriverServiceWebClientAsync {
    private final WebClient webClient;

    public DriverServiceWebClientAsync(WebClient webClient) {
        this.webClient = webClient;
    }

    // WebClient 'ta liste döndürme yöntemleri
    public Mono<List<CarResponseDto>> getAll() {
        Mono<List<CarResponseDto>> listMono = webClient.get()
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<CarResponseDto>>() {
                });
        return listMono;
    }

    public Flux<CarResponseDto> getAll2() {
        Flux<CarResponseDto> carResponseDtoFlux = webClient.get()
                .retrieve()
                .bodyToFlux(CarResponseDto.class);
        return carResponseDtoFlux;
    }

    public Mono<ResponseEntity<List<CarResponseDto>>> getAll3() {
        Mono<ResponseEntity<List<CarResponseDto>>> responseEntityMono = webClient.get()
                .retrieve()
                .toEntityList(CarResponseDto.class);
        return responseEntityMono;
    }

    public Mono<ResponseEntity<List<CarResponseDto>>> getAll4() {
        Mono<ResponseEntity<List<CarResponseDto>>> responseEntityMono = webClient.get()
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<CarResponseDto>>() {});
        return responseEntityMono;
    }


    public Mono<ResponseEntity<CarResponseDto>> getCarById(Integer id) {
        Mono<ResponseEntity<CarResponseDto>> responseEntityMono = webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new CarNotFoundException("Car not found with id " + id)))
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new RuntimeException("Server Error")))
                .toEntity(CarResponseDto.class);
        return responseEntityMono;
    }

    public Mono<ResponseEntity<CarResponseDto>> createCar(CarRequestDto carRequestDto) {
        Mono<ResponseEntity<CarResponseDto>> responseEntityMono = webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(carRequestDto)
                .retrieve()
                .onStatus(status -> status.value() == HttpStatus.BAD_REQUEST.value(),
                        clientResponse -> Mono.error(new CarAlreadyExistException("car already exists ")))
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new RuntimeException("Server Error")))
                .toEntity(CarResponseDto.class);
        return responseEntityMono;
    }

    public Mono<ResponseEntity<CarResponseDto>> updateCar(Integer id, CarRequestDto carRequestDto) {
        Mono<ResponseEntity<CarResponseDto>> responseEntityMono = webClient.put()
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
                .toEntity(CarResponseDto.class);
        return responseEntityMono;
    }

    public Mono<ResponseEntity<Void>> deleteCar(Integer id) {
        Mono<ResponseEntity<Void>> responseEntityMono = webClient.delete()
                .uri("/id/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new CarNotFoundException("Car not found with id " + id)))
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new RuntimeException("Server Error")))
                .toBodilessEntity();
        return responseEntityMono;
    }

    public Mono<ResponseEntity<CarResponseDto>> getCarByModelRequestParam(String model) {
        Mono<ResponseEntity<CarResponseDto>> responseEntityMono = webClient.get()
                .uri("/model?model={model}", model)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new CarNotFoundException("Car not found with model: " + model)))
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new RuntimeException("Server Error")))
                .toEntity(CarResponseDto.class);
        return responseEntityMono;
    }
}
