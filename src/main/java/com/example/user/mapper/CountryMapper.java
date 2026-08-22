package com.example.user.mapper;

import com.example.user.dto.CapitalRequestDto;
import com.example.user.dto.CountryRequestDto;
import com.example.user.dto.CountryResponseDto;
import com.example.user.entity.Country;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CountryMapper {
    Country countryRequestDtoToCountry(CountryRequestDto countryRequestDto);

    @Mapping(source = "capital.id",target = "capitalId")
    @Mapping(source = "capital.name",target = "capitalName")
    CountryResponseDto countryToCountryResponseDto(Country country);
    List<CountryResponseDto> countryToCountryResponseDtoList(List<Country> countryList);
}
