package com.example.user.mapper;

import com.example.user.dto.CarRegisterDto;
import com.example.user.dto.CarResponseDto;
import com.example.user.entity.Car;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CarMapper {
    Car toEntity(CarRegisterDto carRegisterDto);

    CarRegisterDto toDto(Car car);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Car partialUpdate(CarRegisterDto carRegisterDto, @MappingTarget Car car);

    Car toEntity(CarResponseDto carResponseDto);

    List<Car> toEntity(List<CarResponseDto> carResponseDto);

    CarResponseDto toDto1(Car car);

    List<CarResponseDto> toDto(List<Car> car);
}