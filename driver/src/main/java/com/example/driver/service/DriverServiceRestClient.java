package com.example.driver.service;

import com.example.driver.dto.CarRequestDto;
import com.example.driver.dto.CarResponseDto;
import com.example.driver.exception.CarNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ServerErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
public class DriverServiceRestClient {
    private final RestClient restClient;
    public DriverServiceRestClient(RestClient restClient) {
        this.restClient = restClient;
    }


    public CarResponseDto getCarById(Integer id) {
        return restClient.get()
                .uri("/{id}",id)
                .retrieve()
                .body(CarResponseDto.class);
    }

    public ResponseEntity<CarResponseDto> getCarEntityById(Integer id) {
        return restClient.get()
                .uri("/{id}",id)
                .retrieve()
                .toEntity(CarResponseDto.class);
    }

    public ResponseEntity<List<CarResponseDto>> getCars() {
        return restClient.get()
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<CarResponseDto>>(){});
    }
    // baseUrl varsa UriComponentBuilder kullanılmaz çünkü baseUrl 'i devre dışı bırakır.
    public CarResponseDto getCarByIdUriComponentBuilder(Integer id) {
        URI uri = UriComponentsBuilder.fromUriString("http://localhost:8080/api/v1/cars2/{id}")
                .buildAndExpand(id)
                .toUri();

        return restClient.get()
                .uri(uri)
                .retrieve()
                .body(CarResponseDto.class);
    }

    public CarResponseDto createCar(CarRequestDto carRequestDto) {
        return restClient.post()
                .body(carRequestDto)
                .retrieve()
                .body(CarResponseDto.class);
    }

    public ResponseEntity<CarResponseDto> createCarEntity(CarRequestDto carRequestDto) {
        return restClient.post()
                .body(carRequestDto)
                // Gönderdiğimiz verinin türünü belirtmeliyiz
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(CarResponseDto.class);
    }

    public ResponseEntity<CarResponseDto> updateCarEntity(Integer id,CarRequestDto carRequestDto) {

        URI uri = UriComponentsBuilder.fromUriString("http://localhost:8080/api/v1/cars2/id/{id}")
                .buildAndExpand(id)
                .toUri();

        return restClient.put()
                .uri(uri)
                .header("Content-Type","application/json")
                //.contentType(MediaType.APPLICATION_JSON) bu şekilde de yazılabilir.
                // Gönderdiğimiz verinin türünü belirtmeliyiz
                .body(carRequestDto)
                .retrieve()
                .toEntity(CarResponseDto.class);
    }

    public void deleteById(Integer id) {
         restClient.delete()
                .uri("/id/{id}",id)
                .retrieve()
                .toBodilessEntity();
    }

    public CarResponseDto getCarByModelRequestParam(String model) {
        return restClient.get()
                .uri("/model?model={model}",model)
                .retrieve()
                .body(CarResponseDto.class);
    }

    public CarResponseDto getCarByIdException(Integer id) {
        return restClient.get()
                .uri("/{id}",id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,(request, response) -> {
                    if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                        throw new CarNotFoundException("Car not found");
                    }
                    else if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                        throw new BadRequestException("Bad Request");
                    }
                })
                .body(CarResponseDto.class);
    }
}
