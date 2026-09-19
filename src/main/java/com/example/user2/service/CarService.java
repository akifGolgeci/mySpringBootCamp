package com.example.user2.service;

import com.example.user2.dto.CarRequestDto;
import com.example.user2.dto.CarRequestDtoByJPABuddy;
import com.example.user2.dto.CarResponseDto;
import com.example.user2.entity.Car;
import com.example.user2.exception.CarAlreadyExistException;
import com.example.user2.exception.CarNotFoundException;
import com.example.user2.mapper.CarMapper;
import com.example.user2.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CarService {
    private final CarRepository carRepository;
    private final CarMapper carMapper;

    public CarService(CarRepository carRepository, CarMapper carMapper) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
    }


    public List<CarResponseDto> getCars() {
        List<Car> cars = carRepository.findAll();

        if (cars.isEmpty()) {
            throw new CarNotFoundException("Car not found");
        }
        return carMapper.carToCarResponseDtoList(cars);
    }

    public CarResponseDto getCarByModel(String model) {
        Car car = carRepository.findByModel(model).orElseThrow(() -> new CarNotFoundException("Car not found"));

        return carMapper.carToCarResponseDto(car);

    }

    public CarResponseDto createCar(CarRequestDtoByJPABuddy carRequestDto) {
        Optional<Car> car = carRepository.findByModel(carRequestDto.getModel());
        if (car.isPresent()) {
            throw new CarAlreadyExistException("Car already exist");
        }

        Car savedCar = carRepository.save(carMapper.carRequestDtoByJPABuddyToCar(carRequestDto));
        return carMapper.carToCarResponseDto(savedCar);
    }

    public CarResponseDto updateCar(Integer id,CarRequestDto carRequestDto) {
        Car car = carRepository.findById(id).orElseThrow(() -> new CarNotFoundException("Car not found"));

        //YANLIŞ UPDATE KULLANIMI. MAPPER DA KULLANILIDĞI İÇİN PERFORMANSLI DEĞİLDİR.
//        Car car1 = carMapper.carRequestDtoToCar(carRequestDto);
//        car1.setId(car.getId());

        //GÜVENLİ UPDATE TİR DTO OLMAYAN ALANLARIN NULL OLMASINI ÖNLER. DB' DEKİ VERİYİ EZMEZ
        car.setModel(carRequestDto.getModel());
        car.setYear(carRequestDto.getYear());
        car.setPrice(carRequestDto.getPrice());
        car.setBrand(carRequestDto.getBrand());

        Car savedCar = carRepository.save(car);
        return carMapper.carToCarResponseDto(savedCar);
    }

    public void deleteCarByModel(String model) {
        Car car = carRepository.findByModel(model).orElseThrow(() -> new CarNotFoundException("Car not found"));
        carRepository.delete(car);
    }

    public CarResponseDto getCarById(Integer id) {
        return carMapper.carToCarResponseDto(carRepository.findById(id).orElseThrow(() -> new CarNotFoundException("Car not found")));
    }

    public CarResponseDto createCar2(CarRequestDto carRequestDto) {
        Optional<Car> car = carRepository.findByModel(carRequestDto.getModel());
        if (car.isPresent()) {
            throw new CarAlreadyExistException("Car already exist");
        }

        Car savedCar = carRepository.save(carMapper.carRequestDtoToCar(carRequestDto));
        return carMapper.carToCarResponseDto(savedCar);
    }

    public List<String> wordRepeat(String word, Integer repeat) {
        if (repeat == 0) {
            throw new CarNotFoundException("Car not found");
        } else if (word.equalsIgnoreCase("a")) {
            throw new CarAlreadyExistException("Car already exist");

        }
        List<String> list = new ArrayList<>();
        for (int i= repeat;  i > 0; i--) {
            list.add(word);
        }
        return list;
    }

    public void deleteCarById(Integer id) {
        Car car = carRepository.findById(id).orElseThrow(() -> new CarNotFoundException("Car not found"));
        carRepository.delete(car);
    }
}
