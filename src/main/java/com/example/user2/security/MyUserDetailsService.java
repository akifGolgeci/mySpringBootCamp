package com.example.user2.security;

import com.example.user2.entity.User;
import com.example.user2.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // girilen username ile veri tabanındaki username 'i karşılaştırır bulursa UserDetails nesnesi döner.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with this username"));

        MyUserDetails userDetails = new MyUserDetails(user);

        return userDetails;
    }
}
