package com.example.driver.feign;

import com.example.driver.dto.CarRequestDto;
import com.example.driver.dto.CarResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//OpenFeign interfacelerini görebilmesi için DriverApplicationda @EnableFeignClients anatasyonu yazılmalıdır

@org.springframework.cloud.openfeign.FeignClient(name = "user2", url = "http://localhost:8080/api/v1/cars2")
public interface FeignClient {

    @GetMapping
    ResponseEntity<List<CarResponseDto>> findAll();

    @GetMapping("/{id}")
    ResponseEntity<CarResponseDto> findById(@PathVariable("id") Integer id);

    @GetMapping("/model")
    ResponseEntity<CarResponseDto> findByModel(@RequestParam String model);

    @PostMapping
    ResponseEntity<CarResponseDto> create(@RequestBody CarRequestDto carRequestDto);

    @PutMapping("/id/{id}")
    ResponseEntity<CarResponseDto> update(@PathVariable("id") Integer id, @RequestBody CarRequestDto carRequestDto);

    @DeleteMapping("/id/{id}")
    ResponseEntity<Void> delete(@PathVariable("id") Integer id);
}
