package com.example.user.controller;

import com.example.user.dto.SiteRequestDto;
import com.example.user.dto.SiteResponseDto;
import com.example.user.dto.UserResponse;
import com.example.user.entity.Site;
import com.example.user.entity.User;
import com.example.user.mapper.SiteMapper;
import com.example.user.repository.SiteRepository;
import com.example.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class SiteController {
   /* private final UserRepository userRepository;
    private final SiteRepository siteRepository;
    private final SiteMapper siteMapper;

    public SiteController(UserRepository userRepository, SiteRepository siteRepository, SiteMapper siteMapper) {
        this.userRepository = userRepository;
        this.siteRepository = siteRepository;
        this.siteMapper = siteMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SiteResponseDto>  getSite(@PathVariable Long id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            Site site = siteRepository.findById(id).get();

            SiteResponseDto siteResponseDto = new SiteResponseDto();
            UserResponse userResponse = new UserResponse();

            userResponse.setId(site.getId());
            userResponse.setName(site.getName());

            siteResponseDto.setId(site.getId());
            siteResponseDto.setName(site.getName());
            siteResponseDto.setPort(site.getPort());
            siteResponseDto.setUser(userResponse);
            return ResponseEntity.ok(siteResponseDto);
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping
    public ResponseEntity<List<SiteResponseDto>> getAllSite(){
        List<User> users = userRepository.findAll();
        if(users.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        List<Site> sites = siteRepository.findAll();
        return ResponseEntity.ok(siteMapper.siteToSiteResponseList(sites));
    }

    @PostMapping
    public ResponseEntity<SiteResponseDto> addSite(@RequestBody SiteRequestDto siteRequestDto){
        Optional<User> user = userRepository.findById(siteRequestDto.getUser_id());
        if(user.isPresent()){
            Site site = siteRepository.save(siteMapper.siteRequestDtoToSite(siteRequestDto));
            return ResponseEntity.status(HttpStatus.CREATED).body(siteMapper.siteToSiteResponse(site));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public Optional<ResponseEntity<?>> updateSite(@PathVariable Long id, @RequestBody SiteRequestDto siteRequestDto){
        return siteRepository.findById(id).map(site ->  {
            site.setName(siteRequestDto.getName());
            site.setPort(siteRequestDto.getPort());

            if (siteRequestDto.getUser_id() != null) {
                Optional<User> user = userRepository.findById(siteRequestDto.getUser_id());
                user.ifPresent(site::setUser);
                return ResponseEntity.ok(siteMapper.siteToSiteResponse(site));
            }
            return ResponseEntity.notFound().build();
        });

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSite(@PathVariable Long id){
        boolean site = siteRepository.existsById(id);
        if(site){
            siteRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }*/
}
