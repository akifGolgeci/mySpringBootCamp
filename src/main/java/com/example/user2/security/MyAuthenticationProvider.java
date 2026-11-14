package com.example.user2.config;

import com.example.user2.entity.User;
import com.example.user2.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
// Bu sınıfla login yapan kullanıcıyı kontrol ediyoruz
public class MyAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public MyAuthenticationProvider(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // İsteğe bağlı şartlar ekleyebiliriz ( dtoda da yapılabilir).
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //kullanıcın yolladığı Authenticationda username ve password olmalı

//        if (authentication.getCredentials().toString().contains("12345")){
//            throw new BadCredentialsException("Weak Password");
//        }

        String username = authentication.getName();
        // kullanıcıdan geldiği için password null olmaz
        String password = authentication.getCredentials().toString();

        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("Username not found"));
        if(passwordEncoder.matches(password, user.getPassword()) ){
            List<GrantedAuthority> grantedAuthorityList = List.of(new SimpleGrantedAuthority(user.getRoles().name()));

            //şifre null olarak yollanmalı
            return new UsernamePasswordAuthenticationToken(user, null, grantedAuthorityList);
        }
        throw new BadCredentialsException("Password does not match with this user");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));

    }
}