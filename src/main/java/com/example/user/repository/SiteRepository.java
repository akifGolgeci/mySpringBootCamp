package com.example.user.repository;

import com.example.user.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteRepository extends JpaRepository<Site,Long> {
}
