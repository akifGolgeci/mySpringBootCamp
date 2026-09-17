package com.example.user.repository;

import com.example.user.entity.Capital;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CapitalRepository extends JpaRepository<Capital,Long> {
}
