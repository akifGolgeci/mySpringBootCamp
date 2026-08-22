package com.example.user.mapper;

import com.example.user.dto.CapitalRequestDto;
import com.example.user.dto.CapitalResponseDto;
import com.example.user.entity.Capital;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CapitalMapper {
    Capital capitalRequestDtoToCapital(CapitalRequestDto capitalRequestDto);
    CapitalResponseDto capitalToCapitalResponseDto(Capital capital);
    List<CapitalResponseDto> capitalToCapitalResponseDto(List<Capital> capitals);
}
