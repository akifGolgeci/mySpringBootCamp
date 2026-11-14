package com.example.user2.security;

import com.example.user2.dto.RegisterDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
// kullanıcı alcaksak @AuthenticationPrincipal kullanılır,    Rol kontrolü yapıcaksak @CurrentSecurityContext kullanılır
@RestController
@RequestMapping("/api/v1/users2")
public class AuthenticationController {

    private final MyAuthenticationProvider myAuthenticationProvider;
    private final JWTService jWTService;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthenticationController(MyAuthenticationProvider myAuthenticationProvider, JWTService jWTService, UserDetailsService userDetailsService, RefreshTokenRepository refreshTokenRepository) {
        this.myAuthenticationProvider = myAuthenticationProvider;
        this.jWTService = jWTService;
        this.userDetailsService = userDetailsService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

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

    @PostMapping("/login")
    public  ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        try {
            Authentication authentication = myAuthenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(),
                            loginDto.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // Login yaparken hem refresh token hem de JWT(Access token) üretir (Token Rotation)
            TokenPair tokenPair = jWTService.generateTokenPair(userDetails);

            return ResponseEntity.ok(Map.of(
                    "response","Login success",
                    "access token", tokenPair.getAccessToken(),
                    "refresh token", tokenPair.getRefreshToken(),
                    "username", userDetails.getUsername()
            ));
        }
        catch (AuthenticationException e) {
            // Sadece AuthenticationProvider içinden fırlatılan Spring Security hataları
            return ResponseEntity.badRequest().body("Giriş başarısız: " + e.getMessage());
        }
        catch (Exception e) {
            // Kodsal hatalar (ClassCast, NullPointer, JWT hataları) buraya düşer
            e.printStackTrace(); // GERÇEK HATAYI KONSOLA YAZDIRIR
            return ResponseEntity.internalServerError().body("Sunucu tarafında bir hata oluştu: " + e.getMessage());
        }
    }

    // front end(React, Angular, Mobil) tarafından access tokenin süresi dolduğunda
    // yeni access token üretmek istendiği zaman çağrılcak güvenlik amacıyla access token oluşturulduğunda beraberinde
    // refresh token de oluşturulur böylece kullancı sürekli uygulamayı kullanıyorsa refresh tokenin süresi hep taze kalır
    // refresh tokenin çalınma tehlikesi azalır
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            String username = jWTService.refreshAccessToken(request.getToken());
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            TokenPair tokenPair = jWTService.generateTokenPair(userDetails);

            return ResponseEntity.ok(Map.of(
                    "response","Tokens are recreated",
                    "Access Token", tokenPair.getAccessToken(),
                    "Refresh Token", tokenPair.getRefreshToken()
            ));
        }
        catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogOutRequest request) {
        try {
            String username = jWTService.getUsernameFromToken(request.getAccessToken());
            jWTService.removeAllRefreshTokens(username);

            return ResponseEntity.ok("Logout success");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Logout failed ");
        }



    }
}
