package com.example.user2.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
// En sonda Security configde addFilter olarak ekle
@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final MyUserDetailsService myUserDetailsService;

    public JWTFilter(JWTService jwtService, MyUserDetailsService myUserDetailsService) {
        this.jwtService = jwtService;
        this.myUserDetailsService = myUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        // Authorization:Baerer <Token>   şeklinde header gönderilir. Tokendan önce 7 karakter vardır
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request,response);
            return;
        }

        // tokendan öncesini atar ve tokendan gelen usernamei çeker
        String token = header.substring(7);
        String username = jwtService.getUsernameFromToken(token);

        // SecurityContextHolder giriş yapmış kullanıcıları tutar. Sistemde yoksa giriş yaparken uygulamayı kullansın diye token verilir
        if(username != null || SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);

            // token içindeki username ile veritabınında önce kayıt yapmış kullanıcı eşleşiyorsa authentication nesnesi oluşturulup
            // SecurityContextHolder(giriş yapmış kullanıcılara) eklenir
            if (jwtService.isValidToken(token, userDetails)) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request,response);
    }
}





