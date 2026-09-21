package com.example.driver.controller;

import com.example.driver.dto.CarRequestDto;
import com.example.driver.dto.CarResponseDto;
import com.example.driver.service.DriverServiceOpenFeign;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/driver/openFeign")
public class DriverControllerOpenFeign {

    private final DriverServiceOpenFeign driverServiceOpenFeign;

    public DriverControllerOpenFeign(DriverServiceOpenFeign driverServiceOpenFeign) {
        this.driverServiceOpenFeign = driverServiceOpenFeign;
    }

    @GetMapping
    ResponseEntity<List<CarResponseDto>> getAll() {
        return ResponseEntity.ok().body(driverServiceOpenFeign.getAll());
    }

    @GetMapping("/id/{id}")
    ResponseEntity<CarResponseDto> getById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok().body(driverServiceOpenFeign.getById(id));
    }

    @GetMapping("/model")
    ResponseEntity<CarResponseDto> getByModel(@RequestParam("model") String model) {
        return ResponseEntity.ok(driverServiceOpenFeign.getByModel(model));
    }

    @PostMapping
    ResponseEntity<CarResponseDto> create(@RequestBody CarRequestDto carRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(driverServiceOpenFeign.create(carRequestDto));
    }

    @PutMapping("/{id}")
    ResponseEntity<CarResponseDto> update(@PathVariable("id") Integer id, @RequestBody CarRequestDto carRequestDto) {
        return ResponseEntity.ok(driverServiceOpenFeign.update(id, carRequestDto));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<CarResponseDto> delete(@PathVariable("id") Integer id) {
        driverServiceOpenFeign.delete(id);
        return ResponseEntity.noContent().build();
    }
}
