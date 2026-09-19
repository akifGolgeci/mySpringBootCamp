package com.example.driver.controller;

import com.example.driver.dto.CarRequestDto;
import com.example.driver.dto.CarResponseDto;
import com.example.driver.service.DriverServiceRestClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/driver/rest-client")
@RestController
public class DriverControllerRestClient {
    private final DriverServiceRestClient driverServiceRestClient;
    public DriverControllerRestClient(DriverServiceRestClient driverServiceRestClient) {
        this.driverServiceRestClient = driverServiceRestClient;
    }


    @GetMapping
    public ResponseEntity<List<CarResponseDto>> getAllCars() {
        return driverServiceRestClient.getCars();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponseDto> getId(@PathVariable Integer id) {
        return ResponseEntity.ok(driverServiceRestClient.getCarById(id));
    }

    @GetMapping("/exception/{id}")
    public ResponseEntity<CarResponseDto> getIdWithException(@PathVariable Integer id) {
        return ResponseEntity.ok(driverServiceRestClient.getCarByIdException(id));
    }

    @GetMapping("/entity/{id}")
    public ResponseEntity<CarResponseDto> getEntityId(@PathVariable Integer id) {
        return driverServiceRestClient.getCarEntityById(id);
    }

    @GetMapping("/uri/{id}")
    public ResponseEntity<CarResponseDto> getCarByIdUriComponentBuilder(@PathVariable Integer id){
        return ResponseEntity.ok(driverServiceRestClient.getCarByIdUriComponentBuilder(id));
    }

    @GetMapping("/model")
    public ResponseEntity<CarResponseDto> getCarByModelRequestParam(@RequestParam String model){
        return ResponseEntity.ok(driverServiceRestClient.getCarByModelRequestParam(model));
    }

    @PostMapping
    public ResponseEntity<CarResponseDto> createCar(@RequestBody CarRequestDto carRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(driverServiceRestClient.createCar(carRequestDto));
    }

    @PostMapping("/entity")
    public ResponseEntity<CarResponseDto> createCarEntity(@RequestBody CarRequestDto carRequestDto) {
        return driverServiceRestClient.createCarEntity(carRequestDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarResponseDto> updateCar(@PathVariable Integer id, @RequestBody CarRequestDto carRequestDto) {
        return driverServiceRestClient.updateCarEntity(id,carRequestDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteId(@PathVariable Integer id) {
        driverServiceRestClient.deleteById(id);
        return ResponseEntity.noContent().build();

    }


}
