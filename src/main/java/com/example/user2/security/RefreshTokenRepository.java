package com.example.user2.security;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByToken(String token);

    //kullanıcı login yaptığında önceden kullanılmamaış token varsa onu verir böylece yeni gereksiz token üretilmez
    Optional<RefreshTokenRepository> findByUsernameAndIsUsedFalse(String userName);

    @Modifying
    @Transactional
    void deleteByUsername(String userName);

    @Modifying
    @Transactional
    @Query("delete from RefreshTokenEntity as rte where rte.expiryDate < :now")
    void deleteExpiredTokens(LocalDateTime now);

    @Modifying
    @Transactional
    @Query("update RefreshTokenEntity rte set rte.isUsed = true where rte.username = :username")
    void markAllAsUsedByUsername(String username);

    
}
