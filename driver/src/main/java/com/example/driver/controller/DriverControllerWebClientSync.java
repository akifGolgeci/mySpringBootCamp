package com.example.driver.controller;

import com.example.driver.dto.CarRequestDto;
import com.example.driver.dto.CarResponseDto;
import com.example.driver.service.DriverServiceWebClientSync;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/driver/web-client/sync")
public class DriverControllerWebClientSync {

    private final DriverServiceWebClientSync driverServiceWebClientSync;

    public DriverControllerWebClientSync(DriverServiceWebClientSync driverServiceWebClientSync) {
        this.driverServiceWebClientSync = driverServiceWebClientSync;
    }

    //
    @GetMapping
    public ResponseEntity<List<CarResponseDto>> getAllDrivers() {
        return ResponseEntity.ok(driverServiceWebClientSync.getAll());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<CarResponseDto> getCarById(@PathVariable Integer id) {
        return driverServiceWebClientSync.getCarById(id);
    }

    @GetMapping("/model")
    public ResponseEntity<CarResponseDto> findByModelRequestParam(@RequestParam String model) {
        return ResponseEntity.ok(driverServiceWebClientSync.getCarByModelRequestParam(model));
    }

    @PostMapping
    public ResponseEntity<CarResponseDto> createCar(@RequestBody CarRequestDto carRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(driverServiceWebClientSync.createCar(carRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarResponseDto> updateCar(@PathVariable Integer id, @RequestBody CarRequestDto carRequestDto) {
        return driverServiceWebClientSync.updateCar(id, carRequestDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Integer id) {
        return driverServiceWebClientSync.deleteCar(id);
    }
}





