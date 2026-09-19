package com.example.driver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class ConfigSettings {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // 2 FARKLI ŞEKİLDE INTERCEPTOR EKLEME YÖNTEMİ
        restTemplate.getInterceptors().add(new CustomInterceptor());
        //restTemplate.setInterceptors(List.of(new CustomInterceptor()));

        return restTemplate;
    }

    //  "CONTENT-TYPE" POST, PUT, PATCH DE KULLANILIR AMA ACCEPT TÜM CRUD İŞLEMLERİNDE KULLANILIR BU YÜZDEN ACCEPT YAZMAK DAHA MANTIKLIDIR
    // baseUrl varsa UriComponentBuilder kullanılmaz çünkü baseUrl 'i devre dışı bırakır.
    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl("http://localhost:8080/api/v1/cars2")
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .build();
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8080/api/v1/cars2")
                .defaultHeader(HttpHeaders.ACCEPT, String.valueOf(MediaType.APPLICATION_JSON))
                .build();
    }
}
