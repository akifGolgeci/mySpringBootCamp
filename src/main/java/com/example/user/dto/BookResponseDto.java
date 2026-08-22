package com.example.user.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.example.user.entity.Book}
 */
@Value
public class BookResponseDto implements Serializable {
    Integer id;
    String bookName;
}