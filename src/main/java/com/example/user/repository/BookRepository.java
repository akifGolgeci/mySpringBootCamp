package com.example.user.repository;

import com.example.user.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.rest.core.annotation.RepositoryRestResource;
//
//@RepositoryRestResource
public interface BookRepository extends JpaRepository<Book, Integer> {
}
