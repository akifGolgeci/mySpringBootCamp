package com.example.user.service;

import com.example.user.dto.CountryRequestDto;
import com.example.user.dto.CountryResponseDto;
import com.example.user.entity.Country;
import com.example.user.mapper.CountryMapper;
import com.example.user.repository.CapitalRepository;
import com.example.user.repository.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryService {
    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;
    private final CapitalRepository capitalRepository;

    public CountryService(CountryRepository countryRepository, CountryMapper countryMapper, CapitalRepository capitalRepository) {
        this.countryRepository = countryRepository;
        this.countryMapper = countryMapper;
        this.capitalRepository = capitalRepository;
    }

    public List<CountryResponseDto> getAll() {
        List<Country> countryList = countryRepository.findAll();
        if (!countryList.isEmpty()) {
            return countryMapper.countryToCountryResponseDtoList(countryList);
        }
        throw new RuntimeException("Country List is Empty");
    }

    public CountryResponseDto save(CountryRequestDto countryRequestDto) {
        Country country = countryMapper.countryRequestDtoToCountry(countryRequestDto);
        countryRepository.save(country);
        return countryMapper.countryToCountryResponseDto(country);



        /*Optional<Capital> capital = capitalRepository.findById(countryRequestDto.getCapitalId());
        if(capital.isPresent() && countryRequestDto.getCapitalId() != null) {
            Country country = countryRepository.findById(countryRequestDto.getCapitalId()).orElse(null);
            Country updateCountry = countryMapper.countryRequestDtoToCountry(countryRequestDto);
            updateCountry.setId(country.getId());
            updateCountry.setCapital(capital.get());



        }*/
    }
    public CountryResponseDto getById(Long id) {
        Country country = countryRepository.findById(id).orElse(null);
        if (country != null) {
            return countryMapper.countryToCountryResponseDto(country);
        }
        throw new RuntimeException("Country Not Found");
    }

    public CountryResponseDto update(Long id,CountryRequestDto countryRequestDto) {
        Optional<Country> country = countryRepository.findById(id);
        if (country.isPresent()) {
            country.get().setName(countryRequestDto.getName());
            country.get().setPopulation(countryRequestDto.getPopulation());
            Country savedCountry= countryRepository.save(country.get());
            return countryMapper.countryToCountryResponseDto(savedCountry);

        }
        throw new RuntimeException("Country Not Found");
    }

    public Void delete(Long id) {
        Optional<Country> country = countryRepository.findById(id);
        country.ifPresent(countryRepository::delete);
        throw new RuntimeException("Country Not Found");
    }
   /*





    */
}
