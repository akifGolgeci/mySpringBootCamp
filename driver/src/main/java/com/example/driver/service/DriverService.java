package com.example.driver.service;

import com.example.driver.dto.CarResponseDto;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DriverService {

    private final RestTemplate restTemplate;

    public DriverService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CarResponseDto getCar(Integer id) {
        CarResponseDto carResponseDto = restTemplate.getForObject("http://localhost:8080/api/v1/cars2/{id}", CarResponseDto.class, id);
        return carResponseDto;
    }

    public ResponseEntity<?> getCarDetailed(Integer id){
        try {
            // multi queryde, restTemplate.getForEntity("http://localhost:8080/api/v1/cars2/{id}/{id1}/{id2}",
            // CarResponseDto.class, id, id1, id2); şeklinde  yazılabilir
            ResponseEntity<CarResponseDto> carResponseDtoDetailed =
                    restTemplate.getForEntity("http://localhost:8080/api/v1/cars2/{id}", CarResponseDto.class, id);
            return carResponseDtoDetailed;
        }
        catch (HttpClientErrorException e) {
            String responseBodyAsString = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();
            return ResponseEntity.status(statusCode).body(responseBodyAsString);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unknown error: " + e.getMessage());
        }
    }

    public ResponseEntity<?> getCarDetailedWithUriPathVariable(Integer id){
        try {

            URI uri = UriComponentsBuilder.fromUriString("http://localhost:8080/api/v1/cars/{id}")
                    .buildAndExpand(id) // multi queryde,  buildAndExpand(model,model1,model2) şeklinde  yazılabilir
                    .toUri();

            ResponseEntity<CarResponseDto> carResponseDtoDetailedWithUri = restTemplate.getForEntity(uri, CarResponseDto.class);
            return carResponseDtoDetailedWithUri;

        }
        catch (HttpClientErrorException e) {
            String responseBodyAsString = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();
            return ResponseEntity.status(statusCode).body(responseBodyAsString);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unknown error: " + e.getMessage());
        }
    }

    public ResponseEntity<?> getCarDetailedWithMap(Integer id){
        try {
            // multi query verilmişse bunları map içine koyup
            // restTemplate.getForEntity("http://localhost:8080/api/v1/cars2/{id}", CarResponseDto.class, map);
            // şeklinde yazabiliriz.
            Map<String,Integer> map = new HashMap<>();
              map.put("id",id);
//            map.put("id1",id1);
//            map.put("id2",id2);

            ResponseEntity<CarResponseDto> carResponseDtoDetailed =
                    restTemplate.getForEntity("http://localhost:8080/api/v1/cars2/{id}", CarResponseDto.class, map);
            return carResponseDtoDetailed;
        }
        catch (HttpClientErrorException e) {
            String responseBodyAsString = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();
            return ResponseEntity.status(statusCode).body(responseBodyAsString);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unknown error: " + e.getMessage());
        }
    }


    public ResponseEntity<?> getCarDetailed2(String model) {
        try {
            // multi queryde "http://localhost:8080/api/v1/cars2/model?model={model}&model1={model1}&model2={model2}",
            // CarResponseDto.class, model, model1, model2)  şeklinde yazılır.
            // Ayrıca url deki parantez içindeki ifadelere kaçıncı paramtre olduğunun sayısı yazılarak da kullanılabilir
            // Örneğin : model1={model1}&model2={2}&model3={3}
            ResponseEntity<CarResponseDto> responseEntityDetailed = restTemplate.getForEntity(
                    "http://localhost:8080/api/v1/cars2/model?model={model}",
                    CarResponseDto.class,
                    model);
            return responseEntityDetailed;
        }
        catch (HttpClientErrorException e) {
            String responseBodyAsString = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();
            return ResponseEntity.status(statusCode).body(responseBodyAsString);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unknown error: " + e.getMessage());
        }

    }
    public ResponseEntity<?> getCarDetailed2WithURIRequestParam(String model) {
        try {
            URI uri = UriComponentsBuilder.fromUriString("http://localhost:8080/api/v1/cars2/model?model={model}")
                    .queryParam(model)  // multi queryle yazılcaksa her parametre için ayrı queryparam yazılmalı .queryParam(model).queryParam(model2).queryParam(model3)
                    .build()
                    .toUri();

            ResponseEntity<CarResponseDto> responseEntityDetailedWithUsingURI = restTemplate.getForEntity(
                    uri,
                    CarResponseDto.class
                    );
            return responseEntityDetailedWithUsingURI;
        }
        catch (HttpClientErrorException e) {
            String responseBodyAsString = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();
            return ResponseEntity.status(statusCode).body(responseBodyAsString);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unknown error: " + e.getMessage());
        }

    }
    // restTemplate ile bu şekilde liste döndüremeyiz exchange kullanmalıyız
    public List<CarResponseDto> getCarList(){

        return restTemplate.getForObject("http://localhost:8080/api/v1/cars", List.class);
    }


}
