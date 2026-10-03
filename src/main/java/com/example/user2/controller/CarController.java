package com.example.user2.controller;

import com.example.user2.dto.CarRequestDto;
import com.example.user2.dto.CarResponseDto;
import com.example.user2.entity.Car;
import com.example.user2.mapper.CarMapper;
import com.example.user2.repository.CarRepository;
import com.example.user2.service.CarService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/api/v1/cars2")
public class CarController {
    private final CarService carService;
    private final CarRepository carRepository;
    private final CarMapper carMapper;

    public CarController(CarService carService, CarRepository carRepository, CarMapper carMapper) {
        this.carService = carService;
        this.carRepository = carRepository;
        this.carMapper = carMapper;
    }

    // Custom oluşturduğumuz headerı da görmek için buraya @RequestHeader ekliyoruz çünkü custom headerler dış servisten gelir
    @GetMapping
    public ResponseEntity<List<CarResponseDto>> findAll(@RequestHeader("header") HttpHeaders headers ) {
        log.error("error2");
        log.debug("debug2");
        log.info("info2");
        log.trace("trace2");
        headers.forEach((k,v)->{
            System.out.println("header: "+ k + "-> "+ v);
        });
        return ResponseEntity.ok(carService.getCars());
    }

    @GetMapping("/model")
    public ResponseEntity<CarResponseDto> findByModel(@RequestParam String model) {
        return ResponseEntity.ok(carService.getCarByModel(model));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponseDto> findById(@PathVariable @Min(1) Integer id) {
        return ResponseEntity.ok(carService.getCarById(id));
    }
    @GetMapping("/by-id")
    public ResponseEntity<CarResponseDto> findById2(
            @RequestParam @Min(value = 1,message = "id should be equal or higher than 1") Integer id,
            @RequestParam @Min(value = 1,message = "model should be equal or higher than 1") Integer model) {
        return ResponseEntity.ok(carService.getCarById(id));
    }

//    @PostMapping
//    public ResponseEntity<CarResponseDto> create(@Valid @RequestBody CarRequestDtoByJPABuddy carRequestDto) {
//        return ResponseEntity.status(HttpStatus.CREATED).body(carService.createCar(carRequestDto));
//    }

    @PostMapping
    public ResponseEntity<CarResponseDto> create(@Valid @RequestBody CarRequestDto carRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carService.createCar2(carRequestDto));
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<CarResponseDto> update(@PathVariable Integer id, @Valid @RequestBody CarRequestDto carRequestDto) {
        return ResponseEntity.ok(carService.updateCar(id,carRequestDto));
    }

    @DeleteMapping("/model/{model}")
    public ResponseEntity<CarResponseDto> delete(@PathVariable String model) {
        carService.deleteCarByModel(model);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<CarResponseDto> delete2(@PathVariable Integer id) {
        carService.deleteCarById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/word/{repeat}")
    public ResponseEntity<List<String>> wordRepeat(@RequestParam String word, @PathVariable Integer repeat) {
        return ResponseEntity.ok(carService.wordRepeat(word,repeat));
    }

    // PostMapping 'de UriCompenentBuilder ile @RequestParam @RequestBody @PathVariable KULLANIMI ÖRNEĞİ
    @PostMapping("/add/model/{model}")
    public ResponseEntity<CarResponseDto> addCar(@PathVariable String model,
                                                 @RequestParam Integer price,
                                                 @Valid @RequestBody CarRequestDto carRequestDto) {
        Car car = new Car();
        car.setModel(model);
        car.setPrice(price);
        car.setBrand(carRequestDto.getBrand());
        car.setYear(carRequestDto.getYear());

        Car savedCar = carRepository.save(car);
        CarResponseDto carResponseDto = carMapper.carToCarResponseDto(savedCar);

        return ResponseEntity.status(HttpStatus.CREATED).body(carResponseDto);

    }

    @GetMapping("/header")
    public ResponseEntity<?> getHeader(@RequestHeader("header-one")String header1) {
        log.info(header1);
        return ResponseEntity.ok().body(header1);
    }

    @GetMapping("/header2")
    public ResponseEntity<?> getHeader2(@RequestHeader("Authorization")String token) {
        log.info(token);
        return ResponseEntity.ok().body(token);
    }

    @GetMapping("/header3")
    public ResponseEntity<?> getHeader3(@RequestHeader("header-one")String header1) {
        log.info(header1);
        return ResponseEntity.ok()
                .header("result",header1)
                .body(header1);
    }

    @GetMapping("/header4")
    public ResponseEntity<?> getHeader4(@RequestHeader("Authorization")String token) {
        log.info(token);
        return ResponseEntity.ok()
                .header("Authorization",token)
                .body("token HAS BEEN SENT");
    }

    @GetMapping("/header5")
    public ResponseEntity<?> getHeader5() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("test","test");
        headers.add("Authorization","token HAS BEEN SENT");
        return ResponseEntity.ok()
                .headers(headers)
                .body("token HAS BEEN SENT");
    }
}
