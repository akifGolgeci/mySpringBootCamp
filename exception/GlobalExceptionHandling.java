package com.example.user2.exception;

import com.example.user2.dto.ErrorResponseDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.LocalDateTime;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandling {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception ex, ServletWebRequest request) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "SERVER ERROR", ex.getMessage(), request.getRequest().getRequestURI());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDto);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(IllegalArgumentException ex, ServletWebRequest request) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), "ILLEGAL ARGUMENTS",
                ex.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<Map<String,Object>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, ServletWebRequest request) {
        //Class üzeirinde yazan custom anatasyonlarını fieldErrors(); ile yakalayamayız çünkü field erroarlarında anatasyon ,fieldın yani
        //değişkenlerin üzerine yazılır.
        //Eğer hem fielddan hem de class düzeyinde hataları almak istiyosak List<ObjectError> döndüren getAllErrors() metodunu kullanmalıyız
        List<Map<String,Object>> mapList = new ArrayList<>();
        List<ObjectError> errorList = ex.getBindingResult().getAllErrors();
        for (ObjectError error : errorList) {
            Map<String,Object> map = new HashMap<>();
            map.put("message", error.getDefaultMessage());
            map.put("path", request.getRequest().getRequestURI());
            map.put("timestamp", LocalDateTime.now());

            if(error instanceof FieldError fieldError) {
                map.put("field", fieldError.getField());
                map.put("code", fieldError.getCode());
                map.put("rejectedValue", fieldError.getDefaultMessage());
            }
            else {
                map.put("object", error.getObjectName());
            }
            mapList.add(map);

        }
        return ResponseEntity.badRequest().body(mapList);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<Map<String,Object>>> handleConstraintViolationException(ConstraintViolationException ex, ServletWebRequest request) {
        ArrayList<Map<String, Object>> mapList = new ArrayList<>();
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();

        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            Map<String, Object> map = new HashMap<>();
            map.put("path: ", constraintViolation.getPropertyPath().toString());
            map.put("message: ",constraintViolation.getMessage());
            map.put("invalidValue: ", constraintViolation.getInvalidValue());
            map.put("timestamp: ", LocalDateTime.now());
            map.put("URLpath: ", request.getRequest().getRequestURI());
            mapList.add(map);
        }
        return ResponseEntity.badRequest().body(mapList);
    }



    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String,Object>> handleUserNotFoundException(UserNotFoundException ex, ServletWebRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("Status Code: ", HttpStatus.NOT_FOUND.value());
        map.put("Message", ex.getMessage());
        map.put("Path: ", request.getRequest().getRequestURI());
        map.put("TimeStamp: ", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<Map<String,Object>> handleUserAlreadyExistException(UserAlreadyExistException ex, ServletWebRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("Status Code: ", HttpStatus.BAD_REQUEST.value());
        map.put("Message", ex.getMessage());
        map.put("Path: ", request.getRequest().getRequestURI());
        map.put("TimeStamp: ", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
    }

    @ExceptionHandler(CarAlreadyExistException.class)
    public ResponseEntity<ErrorResponseDto>  handleCarAlreadyExistException(CarAlreadyExistException ex, ServletWebRequest request) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(),"Car already exist", ex.getMessage(),
                request.getDescription(false));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);

    }

    @ExceptionHandler(CarNotFoundException.class)
    public ResponseEntity<ErrorResponseDto>  handleCarNotFoundException(CarNotFoundException ex, ServletWebRequest request) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.NOT_FOUND.value(), "Car has not found", ex.getMessage(),
                request.getDescription(false));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }



}
