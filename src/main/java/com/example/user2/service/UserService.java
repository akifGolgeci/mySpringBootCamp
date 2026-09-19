package com.example.user2.service;

import com.example.user2.dto.UserRequestDto;
import com.example.user2.dto.UserResponseDto;
import com.example.user2.entity.User;
import com.example.user2.exception.UserAlreadyExistException;
import com.example.user2.exception.UserNotFoundException;
import com.example.user2.mapper.UserMapper;
import com.example.user2.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserResponseDto getUser(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("user not found with id: " + id));
        return userMapper.userToUserResponse(user);
    }

    public UserResponseDto createUser(long id, UserRequestDto userRequestDto) {
        Optional<User> user1 = userRepository.findById(id);
        if (user1.isPresent()) {
            throw new UserAlreadyExistException("user already exists with id: " + id);
        }
        User userCreated = userMapper.userRequestToDto(userRequestDto);
        userCreated.setId(id);
        User savedUser = userRepository.save(userCreated);

        return userMapper.userToUserResponse(savedUser);
    }
}
