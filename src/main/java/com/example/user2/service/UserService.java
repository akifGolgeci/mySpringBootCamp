package com.example.user2.service;

import com.example.user2.dto.UserRequestDto;
import com.example.user2.dto.UserResponseDto;
import com.example.user2.entity.User;
import com.example.user2.exception.UserAlreadyExistException;
import com.example.user2.exception.UserNotFoundException;
import com.example.user2.mapper.UserMapper;
import com.example.user2.repository.UserRepository;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }
    // sadece kendi bilgilerini görebilir veya ADMINSE görebilir
    @PostAuthorize("returnObject.username == authentication.name or hasRole('ADMIN')")
    public UserResponseDto getUser(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("user not found with id: " + id));
        return userMapper.userToUserResponse(user);
    }

    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        if (userRepository.findByUsername(userRequestDto.getUsername()).isPresent()) {
            throw new UserAlreadyExistException("username already exist");
        }
        User userCreated = userMapper.userRequestToDto(userRequestDto);
        User savedUser = userRepository.save(userCreated);

        return userMapper.userToUserResponse(savedUser);
    }

    public List<UserResponseDto> getUsers() {
        return userMapper.userToUserResponseDto(userRepository.findAll());
    }

    public void deleteUser(long id) {
        userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("user not found with id: " + id));
        userRepository.deleteById(id);
    }
}
