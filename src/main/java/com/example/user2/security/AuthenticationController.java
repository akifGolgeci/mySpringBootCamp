package com.example.user2.controller;

import com.example.user2.config.MyUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Map;
// kullanıcı alcaksak @AuthenticationPrincipal kullanılır,    Rol kontrolü yapıcaksak @CurrentSecurityContext kullanılır
@RestController
@RequestMapping("/api/v1/users2")
public class AuthenticationController {

    @GetMapping("/profile")
    public ResponseEntity<?> profile(@AuthenticationPrincipal MyUserDetails userDetails) {
        String username = userDetails.getUsername();
        GrantedAuthority grantedAuthority = userDetails.getAuthorities().stream().findFirst().orElse(null);

        return ResponseEntity.ok(
                Map.of(
                        "username", username,
                        "authorities", grantedAuthority.getAuthority()
                )
        );
    }

    // AuthenticationPrincipal den farklı bir yöntem
    @GetMapping("/profile2")
    public ResponseEntity<?> profile2() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(
                Map.of(
                        "username", userDetails.getUsername(),
                        "authorities", userDetails.getAuthorities()
                )
        );
    }

    //MyUserDetailse ait olan değişkenleri de çağırabilirim En çok bu kullanılır Özelliştirme varsa
    @GetMapping("/profile3")
    public ResponseEntity<?> profile3() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(
                Map.of(
                        "username", userDetails.getUsername(),
                        "authorities", userDetails.getAuthorities(),
                        "age", userDetails.getAge()
                )
        );
    }

    @GetMapping("/profile4")
    public ResponseEntity<?> profile4() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // kısayol authentication.getName();
        // Object credentials = authentication.getCredentials(); spring security oturum bilgisini tutmadığı için null dönebilir.
        // Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        return ResponseEntity.ok(
                Map.of(
                        "username", authentication.getName(),
                        "authorities", authentication.getAuthorities()
                )
        );
    }

    @GetMapping("/profile5")
    public ResponseEntity<?> profile5(@CurrentSecurityContext SecurityContext securityContext) {
        Authentication authentication = securityContext.getAuthentication();

        return ResponseEntity.ok(
                Map.of(
                        "username", authentication.getName(),
                        "authorities", authentication.getAuthorities()
                ));

    }

    @GetMapping("/profile6")
    public ResponseEntity<?> profile6(@CurrentSecurityContext(expression = "authentication.name") String username) {
        return ResponseEntity.ok(
                Map.of(
                        "username",username
                ));
    }

    @GetMapping("/isAdmin")
    public ResponseEntity<?> isAdmin(@CurrentSecurityContext(expression = "authentication.name.?[authority == 'ROLE_ADMIN'].size() > 0") boolean isAdmin) {
        return ResponseEntity.ok(isAdmin);
    }
}
