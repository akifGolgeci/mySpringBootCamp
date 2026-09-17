package com.example.user.mapper;

import com.example.user.dto.BookRequestDto;
import com.example.user.dto.BookResponseDto;
import com.example.user.entity.Book;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookMapper {
    Book toEntity(BookRequestDto bookRequestDto);

    BookRequestDto toDto(Book book);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Book partialUpdate(BookRequestDto bookRequestDto, @MappingTarget Book book);

    Book toEntity(BookResponseDto bookResponseDto);

    BookResponseDto toDto1(Book book);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Book partialUpdate(BookResponseDto bookResponseDto, @MappingTarget Book book);
}