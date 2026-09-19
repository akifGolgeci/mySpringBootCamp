package com.example.driver.controller;

import com.example.driver.dto.CarRequestDto;
import com.example.driver.dto.CarResponseDto;
import com.example.driver.service.DriverServiceWebClientAsync;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.List;

@RestController
@RequestMapping("/api/v1/driver/web-client/async")
public class DriverControllerWebClientAsync {

    private final DriverServiceWebClientAsync driverServiceWebClientAsync;

    public DriverControllerWebClientAsync(DriverServiceWebClientAsync driverServiceWebClientAsync) {
        this.driverServiceWebClientAsync = driverServiceWebClientAsync;
    }

    @GetMapping
    public Mono<ResponseEntity<List<CarResponseDto>>> getAll2(){
        return driverServiceWebClientAsync.getAll3();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<CarResponseDto>> getById(@PathVariable Integer id){
        return driverServiceWebClientAsync.getCarById(id);
    }

    @GetMapping("/model")
    public Mono<ResponseEntity<CarResponseDto>> getByModel(@RequestParam String model){
        return driverServiceWebClientAsync.getCarByModelRequestParam(model);
    }

    @PostMapping
    public Mono<ResponseEntity<CarResponseDto>> post(@RequestBody CarRequestDto carRequestDto){
        return driverServiceWebClientAsync.createCar(carRequestDto);
    }

    @PutMapping("/id/{id}")
    public Mono<ResponseEntity<CarResponseDto>> put(@PathVariable Integer id, @RequestBody CarRequestDto carRequestDto){
        return driverServiceWebClientAsync.updateCar(id, carRequestDto);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable Integer id){
        return driverServiceWebClientAsync.deleteCar(id);
    }


}
