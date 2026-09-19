package com.example.driver.controller;

import com.example.driver.dto.CarRequestDto;
import com.example.driver.dto.CarResponseDto;
import com.example.driver.service.DriverService;
import com.example.driver.service.DriverService2;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/driver")
@RestController
public class DriverController {

    private final DriverService driverService;
    private final DriverService2 driverService2;

    public DriverController(DriverService driverService, DriverService2 driverService2) {
        this.driverService = driverService;
        this.driverService2 = driverService2;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponseDto> getCar(@PathVariable Integer id) {
        return ResponseEntity.ok(driverService.getCar(id));
    }

    @GetMapping("/detailed/{id}")
    public ResponseEntity<?> getCar2(@PathVariable Integer id) {
        return driverService.getCarDetailed(id);
    }

    @GetMapping("/car")
    public ResponseEntity<?> getCar3(@RequestParam String model) {
        return driverService.getCarDetailed2(model);
    }

    @GetMapping("/exchange/{id}")
    public ResponseEntity<CarResponseDto> getCar4(@PathVariable Integer id) {
        return ResponseEntity.ok(driverService2.getCarList(id));
    }

    @GetMapping("/exchange2")
    public ResponseEntity<CarResponseDto> getCar5(@RequestParam String model) {
        return ResponseEntity.ok(driverService2.getCarByModel(model));
    }

    @GetMapping("/exchange3/word/{repeat}")
    public ResponseEntity<?> exchange(@RequestParam String word, @PathVariable Integer repeat) {
        return ResponseEntity.ok(driverService2.wordRepeat(word,repeat));
    }

    // Karşı serviceden gelen hata mesajlarını json nesnesine dönüştürür veya db'e json kaydetmek için kullanılır
    @GetMapping("/ObjectMapper/{id}")
    public ResponseEntity<?> getCarWithObjectMapper(@PathVariable Integer id) {
        return driverService2.getCarWithObjectMapper(id);
    }

    // Runtimeda generic tiplerin type bilgisi kaybedildiği için getForObject ve getForEntity liste döndüremediğinden exchange kullanılır.
    @GetMapping("/exchangeList")
    public ResponseEntity<Object> getCars() {
        return ResponseEntity.ok(driverService2.getCarsWithExchange());
    }

    @PostMapping("/addCar")
    public ResponseEntity<?> create(@RequestBody CarRequestDto carRequestDto) {
        return driverService2.addCarPostForObject(carRequestDto);
    }

    @PostMapping("/addCar2")
    public ResponseEntity<?> create2(@RequestBody CarRequestDto carRequestDto) {
        return driverService2.addCarPostForEntity(carRequestDto);
    }

    @PostMapping("/addCarExchange")
    public ResponseEntity<?> addCarExchange(@RequestBody CarRequestDto carRequestDto) {
        return driverService2.addCarExchange(carRequestDto);
    }

    @PostMapping("/addCarExchangeWithURI/{model}")
    public ResponseEntity<?> addCarExchangeWithURI(@PathVariable String model,
            @RequestParam Integer price,
            @RequestBody CarRequestDto carRequestDto) {
        return driverService2.addCarExchangeWithURI(model,price,carRequestDto);
    }

    @DeleteMapping("/deleteCar/{id}")
    public ResponseEntity<?> deleteCar(@PathVariable Integer id) {
        return driverService2.deleteCar(id);
    }

    @DeleteMapping("/deleteCarExchange/{id}")
    public ResponseEntity<?> deleteCar2(@PathVariable Integer id) {
        return driverService2.deleteCarExchange(id);
    }

    @PutMapping("updateCar/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,@RequestBody CarRequestDto carRequestDto) {
        return driverService2.updateCar(id,carRequestDto);
    }

    @PutMapping("updateCarExchange/{id}")
    public ResponseEntity<?> update2(@PathVariable Integer id,@RequestBody CarRequestDto carRequestDto) {
        return driverService2.updateCarExchange(id,carRequestDto);
    }



}

