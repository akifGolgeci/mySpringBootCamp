package com.example.user.service;

import com.example.user.dto.CapitalRequestDto;
import com.example.user.dto.CapitalResponseDto;
import com.example.user.entity.Capital;
import com.example.user.mapper.CapitalMapper;
import com.example.user.repository.CapitalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CapitalService {
    private final CapitalMapper capitalMapper;
    private final CapitalRepository capitalRepository;

    public CapitalService(CapitalMapper capitalMapper, CapitalRepository capitalRepository) {
        this.capitalMapper = capitalMapper;
        this.capitalRepository = capitalRepository;
    }

    public CapitalResponseDto save(CapitalRequestDto capitalRequestDto) {
        Capital capital = capitalMapper.capitalRequestDtoToCapital(capitalRequestDto);
        Capital savedCapital = capitalRepository.save(capital);
        return capitalMapper.capitalToCapitalResponseDto(savedCapital);

    }

    public List<CapitalResponseDto> getAll() {
        return capitalMapper.capitalToCapitalResponseDto(capitalRepository.findAll());
    }

    public CapitalResponseDto getById(Long id) {
        if (capitalRepository.existsById(id)) {
            return capitalMapper.capitalToCapitalResponseDto(capitalRepository.findById(id).get());
        }
        throw new RuntimeException("Capital not found");
    }

    public CapitalResponseDto update(Long id, CapitalRequestDto capitalRequestDto) {
        if (capitalRepository.existsById(id)) {
            Capital capital = capitalRepository.findById(id).get();
            Capital updateCapital = capitalMapper.capitalRequestDtoToCapital(capitalRequestDto);

            updateCapital.setId(capital.getId());
            Capital savedCapital= capitalRepository.save(updateCapital);

            return capitalMapper.capitalToCapitalResponseDto(savedCapital);
        }
        throw new RuntimeException("Capital not found");
    }


    public Void delete(Long id) {
        if (capitalRepository.existsById(id)) {
            capitalRepository.deleteById(id);
            return null;
        }
        throw new RuntimeException("Capital not found");
    }
}
