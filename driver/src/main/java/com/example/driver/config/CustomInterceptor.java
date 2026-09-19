package com.example.driver.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

@Slf4j
public class CustomInterceptor implements ClientHttpRequestInterceptor{
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        request.getHeaders().add("custom-header", "custom-value");
        request.getHeaders().add("Authorization", "Bearer token");
        request.getHeaders().add("Content-Type", "application/json");

        log.info("request URI {}",request.getURI());
        log.info("http method {}",request.getMethod());
        log.info("headers {}",request.getHeaders());

        return execution.execute(request,body);
    }
}
