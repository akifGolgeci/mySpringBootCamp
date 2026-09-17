package com.example.user.controller;

import com.example.user.dto.CapitalRequestDto;
import com.example.user.dto.CapitalResponseDto;
import com.example.user.entity.Capital;
import com.example.user.service.CapitalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class CapitalController {

    private final CapitalService capitalService;

    public CapitalController(CapitalService capitalService) {
        this.capitalService = capitalService;
    }

    @PostMapping
    public ResponseEntity<CapitalResponseDto> saveCapital(@RequestBody CapitalRequestDto capitalRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(capitalService.save(capitalRequestDto));
    }

    @GetMapping
    public ResponseEntity<List<CapitalResponseDto>> getAllCapital() {
        return ResponseEntity.ok(capitalService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CapitalResponseDto> getCapital(@PathVariable Long id) {
        return ResponseEntity.ok(capitalService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CapitalResponseDto> updateCapital(@PathVariable Long id,
                                                            @RequestBody CapitalRequestDto capitalRequestDto) {
        return ResponseEntity.ok(capitalService.update(id,capitalRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCapital(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(capitalService.delete(id));
    }
}
