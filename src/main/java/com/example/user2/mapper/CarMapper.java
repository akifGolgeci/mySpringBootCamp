package com.example.user2.mapper;

import com.example.user2.dto.CarRequestDto;
import com.example.user2.dto.CarRequestDtoByJPABuddy;
import com.example.user2.dto.CarResponseDto;
import com.example.user2.entity.Car;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CarMapper {
    Car carRequestDtoToCar(CarRequestDto carRequestDto);
    Car carRequestDtoByJPABuddyToCar(CarRequestDtoByJPABuddy carRequestDto);
    CarResponseDto carToCarResponseDto(Car car);
    List<CarResponseDto> carToCarResponseDtoList(List<Car> cars);
}
