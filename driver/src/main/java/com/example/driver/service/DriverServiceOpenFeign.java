package com.example.driver.service;

import com.example.driver.dto.CarRequestDto;
import com.example.driver.dto.CarResponseDto;
import com.example.driver.feign.FeignClient;
import feign.Client;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class DriverServiceOpenFeign {


    private final FeignClient feignClient;

    public DriverServiceOpenFeign(FeignClient feignClient) {
        this.feignClient = feignClient;
    }

    public List<CarResponseDto> getAll() {
        ResponseEntity<List<CarResponseDto>> feignClientAll = feignClient.findAll();
        return feignClientAll.getBody();
    }

    public CarResponseDto getById(Integer id) {
        return feignClient.findById(id).getBody();
    }

    public CarResponseDto getByModel(String model) {
        return feignClient.findByModel(model).getBody();
    }

    public CarResponseDto create(CarRequestDto carRequestDto) {
        return feignClient.create(carRequestDto).getBody();
    }

    public CarResponseDto update(Integer id, CarRequestDto carRequestDto) {
        return feignClient.update(id, carRequestDto).getBody();
    }

    public void delete(Integer id) {
        feignClient.delete(id);
    }
}
