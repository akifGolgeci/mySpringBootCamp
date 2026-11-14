package com.example.user2.security;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@EnableScheduling
public class TokenCleanUpTask {

    private final JWTService jWTService;

    public TokenCleanUpTask(JWTService jWTService) {
        this.jWTService = jWTService;
    }
    // HER GECE 2'DE SÜRESİ GEÇMİŞ TOKENLERİ TEMİZLE
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanupTokens() {
        jWTService.cleanExpiredRefreshTokens();
        System.out.println("Cleaned up refresh tokens: "+ new Date());
    }
}
