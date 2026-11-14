package com.example.user2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableCaching
@EnableMethodSecurity(prePostEnabled = true) // methodlara @PreAuthorize ve @PostAuthorize(Service ve Controller sınıflarında
                                             // kullanılır.) anatasyonlarının eklenmesini sağlar.
@SpringBootApplication
public class User2Application {

    public static void main(String[] args) {
        SpringApplication.run(User2Application.class, args);
    }

}
