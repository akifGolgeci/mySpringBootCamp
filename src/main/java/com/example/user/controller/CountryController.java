package com.example.user.controller;

import com.example.user.dto.CountryRequestDto;
import com.example.user.dto.CountryResponseDto;
import com.example.user.service.CountryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/country/api/v1")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public ResponseEntity<List<CountryResponseDto>> getAll(){
        return ResponseEntity.ok(countryService.getAll());
    }

    /*@GetMapping("/{id}")
    public ResponseEntity<CountryResponseDto> getOne(@PathVariable Long id){
        return ResponseEntity.ok(countryService.getById(id));
    }

    @PostMapping
    public ResponseEntity<CountryResponseDto> create(@RequestBody CountryRequestDto countryRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(countryService.save(countryRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CountryResponseDto> update(@PathVariable Long id,
                                                     @RequestBody CountryRequestDto countryRequestDto){
        return ResponseEntity.ok(countryService.update(countryRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(countryService.delete(id));
    }*/


}
