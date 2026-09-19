package com.example.driver.service;

import com.example.driver.dto.CarRequestDto;
import com.example.driver.dto.CarResponseDto;
import com.example.driver.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DriverService2 {

    //HTTP HEADERS 'da content-type post istekleri , accept get istekleri için kullanılır

    private final RestTemplate restTemplate;

    public DriverService2(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public CarResponseDto getCarList(Integer id){
        String path = "http://localhost:8080/api/v1/cars2/{id}";

        URI uri = UriComponentsBuilder.fromUriString(path).buildAndExpand(id).toUri();

        HttpHeaders headers = new HttpHeaders();
        // default olarak json alır ama manuel yazmak daha güvenlidir
        headers.put("Content-Type", List.of(String.valueOf(MediaType.APPLICATION_JSON)));

        //Body dönmediğimiz için Void verdik.
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<CarResponseDto> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                requestEntity,
                CarResponseDto.class
        );
        return responseEntity.getBody();

    }

    public CarResponseDto getCarByModel(String model){
        String path = "http://localhost:8080/api/v1/cars2/model";

        URI uri = UriComponentsBuilder.fromUriString(path).queryParam("model",model).build().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.put("Content-Type", List.of(String.valueOf(MediaType.APPLICATION_JSON)));

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<CarResponseDto> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                requestEntity,
                CarResponseDto.class
        );

        return responseEntity.getBody();
    }


    public ResponseEntity<String> wordRepeat(String word, Integer repeat) {


        try {
            String path = "http://localhost:8080/api/v1/cars2/word/{repeat}";
            URI uri = UriComponentsBuilder.fromUriString(path).queryParam("word", word).buildAndExpand(repeat).toUri();

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    String.class
            );
            return responseEntity;
        }
        // karşı servisten gelen 4xx hatalarını alır
        catch (HttpClientErrorException e) {
            String body = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();
            return ResponseEntity.status(statusCode).body(body);
        }

        // karşı servisten gelen 5xx hatalarını alır
        catch (HttpServerErrorException e){
            String body = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();
            return ResponseEntity.status(statusCode).body(body);
        }

        // hem 4xx hem de 5xx hatalarını alır bu kullanılcaksa HttpServerErrorException ve HttpClientErrorException kullanılmaz
        // çünkü aynı görevleri görürler ve daha kısa kullanım örneğidir
        catch (HttpStatusCodeException e) {
            String body = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();
            return ResponseEntity.status(statusCode).body(body);
        }

        catch (ResourceAccessException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Something went wrong" + e.getMessage());
        }



    }

    public ResponseEntity<?> getCarWithObjectMapper(Integer id) {
        try {
            String path = "http://localhost:8080/api/v1/cars2/{id}";
            URI uri = UriComponentsBuilder.fromUriString(path).buildAndExpand(id).toUri();

            ResponseEntity<CarResponseDto> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, null, CarResponseDto.class);
            return responseEntity;
        }
        catch (HttpClientErrorException e) {
            String body = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();

            ObjectMapper mapper = new ObjectMapper();
            ErrorResponseDto errorResponseDto = mapper.readValue(body, ErrorResponseDto.class);

            log.error("Error occurred: {}", errorResponseDto.getMessage());

            return ResponseEntity.status(statusCode).body(errorResponseDto);
        }
    }

    public Object getCarsWithExchange() {
        try {
            String path = "http://localhost:8080/api/v1/cars2";
            URI uri = UriComponentsBuilder.fromUriString(path).build().toUri();


            ResponseEntity<List<CarResponseDto>> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new  ParameterizedTypeReference<List<CarResponseDto>>() {});
            return responseEntity.getBody();
        }
        catch (HttpClientErrorException e) {
            String body = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();
            ObjectMapper mapper = new ObjectMapper();
            ErrorResponseDto errorResponseDto = mapper.readValue(body, ErrorResponseDto.class);
            return ResponseEntity.status(statusCode).body(errorResponseDto);
        }
    }

    public ResponseEntity<?> addCarPostForObject(CarRequestDto carRequestDto) {

        try {
            String path = "http://localhost:8080/api/v1/cars2";

            CarResponseDto carResponseDto = restTemplate.postForObject(path, carRequestDto, CarResponseDto.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(carResponseDto);
        }
        catch (HttpClientErrorException e) {
            String body = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();
            return ResponseEntity.status(statusCode).body(body);
        }
    }

    public ResponseEntity<?> addCarPostForEntity(CarRequestDto carRequestDto) {

        try {
            String path = "http://localhost:8080/api/v1/cars2";

            ResponseEntity<CarResponseDto> responseEntity = restTemplate.postForEntity(path, carRequestDto, CarResponseDto.class);
            return responseEntity;
        }
        catch (HttpClientErrorException e) {
            String body = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();
            return ResponseEntity.status(statusCode).body(body);
        }
    }

    public ResponseEntity<?> addCarExchange(CarRequestDto carRequestDto) {
        try {
            String path = "http://localhost:8080/api/v1/cars2";

            HttpHeaders headers = new HttpHeaders();
            headers.put("Content-Type", List.of(String.valueOf(MediaType.APPLICATION_JSON)));

            HttpEntity<CarRequestDto> requestEntity = new HttpEntity<>(carRequestDto,headers);

            ResponseEntity<CarResponseDto> responseEntity = restTemplate.exchange(path,
                    HttpMethod.POST,
                    requestEntity,
                    CarResponseDto.class);

            return responseEntity;

        }
        catch (HttpClientErrorException e) {
            String body = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();
            return ResponseEntity.status(statusCode).body(body);
        }
    }
    // @RequestParam ve @PathVariable UriComponentsBuilder içinde yazılır.
    // @RequestBody ve Headers HttpEntity içinde yazılır.
    public ResponseEntity<?> addCarExchangeWithURI(String model, Integer price, CarRequestDto carRequestDto) {
        try {
            String path = "http://localhost:8080/api/v1/cars2/add/model/{model}";
            HttpHeaders headers = new HttpHeaders();
            headers.put("Content-Type", List.of(String.valueOf(MediaType.APPLICATION_JSON)));

            HttpEntity<CarRequestDto> requestEntity = new HttpEntity<>(carRequestDto,headers);

            URI uri = UriComponentsBuilder.fromUriString(path).queryParam("price", price).buildAndExpand(model).toUri();

            ResponseEntity<CarResponseDto> responseEntity = restTemplate.exchange(uri,
                    HttpMethod.POST,
                    requestEntity,
                    CarResponseDto.class
            );
            return responseEntity;
        }
        catch (HttpClientErrorException e) {
            String body = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();
            return ResponseEntity.status(statusCode).body(body);
        }
    }

    public ResponseEntity<?> deleteCar(Integer id) {
        try {
            String path = "http://localhost:8080/api/v1/cars2/id/{id}";

            restTemplate.delete(path,id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            String body = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();
            return ResponseEntity.status(statusCode).body(body);
        }


    }

    public ResponseEntity<?> deleteCarExchange(Integer id) {

        try {
            String path = "http://localhost:8080/api/v1/cars2/id/{id}";

            URI uri = UriComponentsBuilder.fromUriString(path).buildAndExpand(id).toUri();

            ResponseEntity<Void> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.DELETE,
                    null,
                    Void.class);

            return responseEntity;
        }
        //
        catch (HttpStatusCodeException e) {
            String body = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();
            return ResponseEntity.status(statusCode).body(body);
        }
    }

    //Exchange olmadan put metodu kullanılırsa body dönmez!!
    public ResponseEntity<?> updateCar(Integer id, CarRequestDto carRequestDto) {

        try {
            String path = "http://localhost:8080/api/v1/cars2/id/{id}";

            URI uri = UriComponentsBuilder.fromUriString(path).buildAndExpand(id).toUri();

            HttpEntity<CarRequestDto> requestEntity = new HttpEntity<>(carRequestDto);

            restTemplate.put(uri,requestEntity);

            Map<String,String> responseMap = new HashMap<>();
            responseMap.put("message ", "Car updated successfully");

            return ResponseEntity.ok().body(responseMap);
        }
        catch (HttpStatusCodeException e){
            String body = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();
            return ResponseEntity.status(statusCode).body(body);
        }
    }

    public ResponseEntity<?> updateCarExchange(Integer id, CarRequestDto carRequestDto) {
        try {
            String path = "http://localhost:8080/api/v1/cars2/id/{id}";
            URI uri = UriComponentsBuilder.fromUriString(path).buildAndExpand(id).toUri();

            HttpEntity<CarRequestDto> requestEntity = new HttpEntity<>(carRequestDto);

            ResponseEntity<CarResponseDto> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.PUT,
                    requestEntity,
                    CarResponseDto.class);

            return responseEntity;
        }
        catch (HttpStatusCodeException e){
            String body = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();
            return ResponseEntity.status(statusCode).body(body);
        }
    }
}
