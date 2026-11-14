package com.example.user2;

import com.example.user2.dto.UserRequestDto;
import com.example.user2.dto.UserResponseDto;
import com.example.user2.entity.RolesEnum;
import com.example.user2.entity.User;
import com.example.user2.exception.UserAlreadyExistException;
import com.example.user2.exception.UserNotFoundException;
import com.example.user2.mapper.UserMapper;
import com.example.user2.repository.UserRepository;
import com.example.user2.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserRequestDto userRequestDto;

    @BeforeEach
    public void setUp() {
         userRequestDto = new UserRequestDto("akif", "123", 17, 1500);
         user = new User(1L, "akif", "123", 17, 1500, RolesEnum.ROLE_USER,
                true, true, true, true);
    }

    @Test
    void findUserById_ShouldReturnUserResponseDto() {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(1L);
        userResponseDto.setUsername("akif");

        // hazırlık aşaması mock nesnemizin ne yapması gerektiğini biz belirliyoruz
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.userToUserResponse(any(User.class))).thenReturn(userResponseDto);

        // hangi metotta işlem yapıcağımızı yazıyoruz
        UserResponseDto resultUser = userService.getUser(1L);

        // dönen nesneyi doğruluyoruz
        assertThat(resultUser, notNullValue());
        assertThat(resultUser.getUsername(),equalTo("akif"));

        // metodun 1 kere çağrıldığını onayla
        verify(userRepository,times(1)).findById(user.getId());
        verify(userMapper,times(1)).userToUserResponse(any(User.class));
    }

    @Test
    void findUserById_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser(2L));

        verify(userRepository,times(1)).findById(2L);
        verify(userMapper,never()).userToUserResponse(any(User.class));
    }

    @Test
    void createUser_UserNameNotInTheRepository_ShouldReturnUserResponseDto() {


        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(1L);
        userResponseDto.setUsername("akif");

        when(userRepository.findByUsername(userRequestDto.getUsername())).thenReturn(Optional.empty());
        when(userMapper.userToUserResponse(any(User.class))).thenReturn(userResponseDto);
        when(userMapper.userRequestToDto(userRequestDto)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDto responseDto = userService.createUser(userRequestDto);

        assertThat(responseDto, notNullValue());
        assertThat(responseDto.getUsername(),equalTo("akif"));

        verify(userRepository,times(1)).findByUsername(userRequestDto.getUsername());
        verify(userMapper,times(1)).userToUserResponse(any(User.class));
        verify(userRepository,times(1)).save(any(User.class));
        verify(userMapper,times(1)).userRequestToDto(userRequestDto);
    }

    @Test
    public void deleteUser_ShouldReturnVoid() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository,times(1)).deleteById(1L);
    }
}
