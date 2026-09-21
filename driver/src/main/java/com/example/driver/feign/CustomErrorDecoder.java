package com.example.driver.feign;

import com.example.driver.exception.CarAlreadyExistException;
import com.example.driver.exception.CarNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == HttpStatus.BAD_REQUEST.value()) {
            return new CarAlreadyExistException("Car already exist");
        }
        else if (response.status() == 404) {
            return new CarNotFoundException("Car not found");
        }
        return defaultErrorDecoder.decode(methodKey, response);
    }
}
