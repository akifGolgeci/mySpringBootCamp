package com.example.user2.service;

import com.example.user2.dto.CarRequestDto;
import com.example.user2.dto.CarRequestDtoByJPABuddy;
import com.example.user2.dto.CarResponseDto;
import com.example.user2.entity.Car;
import com.example.user2.exception.CarAlreadyExistException;
import com.example.user2.exception.CarNotFoundException;
import com.example.user2.mapper.CarMapper;
import com.example.user2.repository.CarRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
// Genellikle service sınıfında caching anatasyonları kullanılır
// listelerde caching yaparken paging veya specification ekleyeceksek key de belirtmeliyiz.
// rediste aynı anda aynı antasyonu kullanamayız. @Caching anatasyonu içinde yazılır
public class CarService {
    private final CarRepository carRepository;
    private final CarMapper carMapper;

    public CarService(CarRepository carRepository, CarMapper carMapper) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
    }
    // 1. cacheNames ve Value aynı işlevi görür
    // 2. @Cacheable(cacheNames = "cars") keysiz yazarsak cachede cars:: bu şekilde tutulur
    // 3. @Cacheable(cacheNames = "cars", key = "{#category, #page, #pageSize}")
    // keyle yazarsa cachede cars::[Sport cars, 0, 10] şeklinde tutulur
    // 4. @Cacheable(cacheNames = "cars", key = "#root.methodName") metod adıyla tutmak isityorsak kullanılır
    // cars::getCars şeklinde cachede tutulur.

    @Cacheable(cacheNames = "cars")   // cars::
    public List<CarResponseDto> getCars() {
        log.info("getCars() executed ");
        List<Car> cars = carRepository.findAll();

        if (cars.isEmpty()) {
            throw new CarNotFoundException("Car not found");
        }
        return carMapper.carToCarResponseDtoList(cars);
    }

    public CarResponseDto getCarByModel(String model) {
        log.info("getCarByModel() called ");
        Car car = carRepository.findByModel(model).orElseThrow(() -> new CarNotFoundException("Car not found"));

        return carMapper.carToCarResponseDto(car);
    }

    // yeni ürün eklendiğinde liste değişir bu yüzden liste silinir
    @CacheEvict(value = "cars", allEntries = true)
    public CarResponseDto createCar(CarRequestDtoByJPABuddy carRequestDto) {
        log.info("createCar() called ");
        Optional<Car> car = carRepository.findByModel(carRequestDto.getModel());
        if (car.isPresent()) {
            throw new CarAlreadyExistException("Car already exist");
        }

        Car savedCar = carRepository.save(carMapper.carRequestDtoByJPABuddyToCar(carRequestDto));
        return carMapper.carToCarResponseDto(savedCar);
    }

    //sadece @CachePut kullanırsak idyi günceller cars::2  cachedeki cars listesini güncellemez bu yüzden cacheteki
    // listeyi silmeliyiz. ki tüm listeyi döndürmek istediğimizde cachedeki eski carsList verisin görmeyelim
    @Caching(
            put = { @CachePut(value = "car", key = "#id")},
            evict = { @CacheEvict(value = "cars", allEntries = true)}
    )
    public CarResponseDto updateCar(Integer id,CarRequestDto carRequestDto) {
        log.info("updateCar() called ");
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
        CarResponseDto carResponseDto = carMapper.carToCarResponseDto(savedCar);
        return carResponseDto;
    }


    public void deleteCarByModel(String model) {
        log.info("deleteCarByModel() called ");
        Car car = carRepository.findByModel(model).orElseThrow(() -> new CarNotFoundException("Car not found"));
        carRepository.delete(car);
    }

    // Sadece metottaki returnü çalıştırır metodun içini çalıştırmaz
    @Cacheable(value = "car", key = "#id") // car::1     car::2     şeklinde cache 'e kaydedilir.
    public CarResponseDto getCarById(Integer id) {
        log.info("getCarById() called ");
        return carMapper.carToCarResponseDto(carRepository.findById(id).orElseThrow(() -> new CarNotFoundException("Car not found")));
    }

    public CarResponseDto createCar2(CarRequestDto carRequestDto) {
        log.info("createCar2() called ");
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

    // cachingle yazmalıyız yoksa çalışmaz.
    @Caching(evict = {
            @CacheEvict(value = "car", key = "#id"), @CacheEvict(value = "cars", allEntries = true)
    })
    public void deleteCarById(Integer id) {
        log.info("deleteCarById() called ");
        Car car = carRepository.findById(id).orElseThrow(() -> new CarNotFoundException("Car not found"));
        carRepository.delete(car);
    }
}
