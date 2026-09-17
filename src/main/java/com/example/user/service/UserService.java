package com.example.user.service;

import com.example.user.dto.UserRequest;
import com.example.user.dto.UserResponse;
import com.example.user.entity.Role;
import com.example.user.entity.User;
import com.example.user.repository.RoleRepository;
import com.example.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public User save(User user) {
        Role role = new Role();
        role.setRoleName("ROLE_USER");
        role.setUser(user);

        if (user.getRole() == null) {
            List<Role> roles = new ArrayList<>();
            user.setRole(roles);
            user.getRole().add(role);
        }
        else {
            user.getRole().add(role);
        }

        userRepository.save(user);
        return user;
    }

    /*public Void delete(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        }
        else  {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        return null;
    }*/

    public Void delete(Long roleid) {
        if (roleRepository.existsById(roleid)) {
            Role role = roleRepository.findById(roleid).get();
            if (role.getUser() != null) {
                User user = role.getUser();

                user.getRole().remove(role);  //user listesinde silinmesi istenen rol ü çıkarttık
                role.setUser(null); // rolde userı çıkarttık
                userRepository.save(user); //kullanıcının yeni halini kaydettik

                //orphanRemoval= true yaptığımız için bunu yazmamıza gerek kalmıyor
                //roleRepository.deleteById(roleid);
            }
        }
        return null;
    }





































    /*private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, " Users not found");
        }
        return userMapper.toUserResponseList(users);
    }

    public UserResponse getUserById(int id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, " User not found");
        }
        return userMapper.toUserResponse(user);

    }

    public UserResponse addUser(UserRequest userRequest) {
        User savedUser = userMapper.toUser(userRequest);
        userRepository.save(savedUser);

        return userMapper.toUserResponse(savedUser);
    }

    public UserResponse updateUser(UserRequest userRequest, int id) {
        User savedUser = userRepository.findById(id).orElse(null);
        if (savedUser == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, " User not found");
        }
        User updatedUser = userMapper.toUser(userRequest);
        updatedUser.setId(savedUser.getId());
        userRepository.save(updatedUser);

        return userMapper.toUserResponse(updatedUser);
    }
    public UserResponse deleteUser(int id) {
        User savedUser = userRepository.findById(id).orElse(null);
        if (savedUser == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, " User not found");
        }
        userRepository.delete(savedUser);
        return userMapper.toUserResponse(savedUser);

    }*/
}
