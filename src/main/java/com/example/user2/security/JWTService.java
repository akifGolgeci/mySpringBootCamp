package com.example.user2.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JWTService {

    // JWT 3 yapıdan oluşur header, payload ve signature

    // Random String Generator sitesinden üretilen secret key normalde environment varable olarak saklanmlaı
    // burada açıkca yazılmamalı
    private final String SECRET_KEY = "XLFVbnxrCCw/O1nlx+sREPAXOx+pbfpUoiMH8nDt82627e24udfmbpBPdSnoiFmxdT5NTlxHBVbgIgJpOcDcLg==";
    private final long ACCESS_TOKEN_VALIDITY_SECONDS = 1000 * 60 * 15; // FOR JWT TOKEN
    private final long REFRESH_TOKEN_VALIDITY_DAYS = 7;                // FOR REFRESH TOKEN
    private final RefreshTokenRepository refreshTokenRepository;

    public JWTService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    // JWT create
    public String generateToken(UserDetails userDetails) {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        List<String> roles = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        return Jwts
                .builder()
                .header().add("typ","JWT").and()                                             // Optiaonaldır JWT headera typ eklenir
                .subject(userDetails.getUsername())                                          // konu ( genelde kullanıcı adı verilir.)
                .signWith(getSecretKey())                                                    // token imzası headerı ve payloadı şifreleme
                .issuedAt(new Date(System.currentTimeMillis()))                              // tokenin oluşturlma tarihi
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS))      // tokenin zaman aşımı tarihi(15dk)
                .claim("roles", roles)                                                  // token eklenti eklenebilir.
                .compact();                                                                  // tokeni oluşturur

    }

    // Refresh Token create
    public RefreshTokenEntity generateRefreshToken(String username) {
        // Kullanıcının önceden kullandığı tokenleri işaretle.
        refreshTokenRepository.markAllAsUsedByUsername(username);

        String token = UUID.randomUUID() + "-" + System.currentTimeMillis();
        LocalDateTime expiryDate = LocalDateTime.now().plusDays(REFRESH_TOKEN_VALIDITY_DAYS);

        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity(token,username, expiryDate);

        return refreshTokenRepository.save(refreshTokenEntity);
    }

    // JWT and Refresh Token create, sadece Login anında çalışır
    public TokenPair generateTokenPair(UserDetails userDetails) {
        String accessToken = generateToken(userDetails);
        RefreshTokenEntity refreshToken = generateRefreshToken(userDetails.getUsername());

        return new TokenPair(accessToken, refreshToken.getToken());

    }

    // login olduktan sonra refresh tokenin var olup olamadığına ve geçerliliğene bakar yoksa front end tarafından
    // login ekranına geri atılır. Geçerli tokense isUsed true olarak işaretler AuthenticationControllerda
    // jwt generate metodu parametresinde kullanılmak için username döner.
    public String refreshAccessToken(String refreshTokenValue) {
        RefreshTokenEntity refreshToken = refreshTokenRepository.findByToken(refreshTokenValue).orElseThrow(
                () -> new RuntimeException("Refresh token not found"));

        // refreshTokenin süresi dolduysa veri tabanından silinmeli front end tarafından log outa atılmalı
        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Token has expired");
        }
        // refresh token isUsed değişkeni loginde false olarak verilir . Eğer bu truesa demek ki hacker tarafından çalınmıştır demektir
        if(refreshToken.isUsed()) {
            throw new RuntimeException("Token has been used");
        }

        // Refresh token ile yeni Acccess token üretmek için kullandığımız için refresh token artık kullanılmış oluyor.
        refreshToken.setUsed(true);
        refreshTokenRepository.save(refreshToken);

        return refreshToken.getUsername();
    }


    private SecretKey getSecretKey() {
        byte[] decode = Decoders.BASE64.decode(SECRET_KEY);
        SecretKey secretKey = Keys.hmacShaKeyFor(decode);

        return secretKey;
    }

    public String getUsernameFromToken(String token) {
        return Jwts
                .parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public Date getExpirationDateFromToken(String token) {
        return Jwts
                .parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }

    public boolean isExpiredToken(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    public boolean isValidToken(String token, UserDetails userDetails) {
        String usernameFromToken = getUsernameFromToken(token);

        return (usernameFromToken.equals(userDetails.getUsername()) && !isExpiredToken(token));
    }

    public List<String> getRolesFromToken(String token) {
        Claims payload = Jwts
                .parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return payload.get("roles",  List.class);
    }

    public boolean isValidRefreshToken(String refreshTokenValue) {
        return refreshTokenRepository.findByToken(refreshTokenValue)
                .map(token -> !token.isUsed() && !token.isExpired())
                .orElse(false);
    }

    // kullanıcı log out yaptığında tüm refresh tokenlerini sil
    public void removeAllRefreshTokens(String username) {
        refreshTokenRepository.deleteByUsername(username);
    }

    // Süresi dolmuş tokenleri temizle
    public void cleanExpiredRefreshTokens() {
        refreshTokenRepository.deleteExpiredTokens(LocalDateTime.now());
    }

}
