package com.example.user2.config;

import com.example.user2.security.CSRFHeaderFilter;
import com.example.user2.security.JWTFilter;
import com.example.user2.security.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;

@Configuration
public class SecurityConfig {
    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private JWTFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // sadece csrf 'i kapatmazsak sadece get işlemini user ve şifreyle yapabiliriz. PUT,DELETE,POST için csrf 'i kapatmalıyız.
        // sadece csrf 'i kapatıp authorizeHttpRequests ve authorization yöntemi(JWT, basicAuth) yazmassak security bozuluyor. user ve şifre girmeden request gönderebiliyoruz.
        return httpSecurity
                //csrf 'i cookie olarak tarayıcıya yollar withonlyHttpFalse ise JavaScript tarafından erişilebilir olduğunu
                // söyler ama saldırı riskini arttırır.
                // Angular ve React gibi Single path front endlerde kullanılır.
                .csrf((AbstractHttpConfigurer::disable))
//                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                        .ignoringRequestMatchers("/api/v1/users2/login","/api/v1/users2/homePage",
//                                "/api/v1/users2/register","/api/v1/users2/login2","/api/v1/users2")
//                )

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class )
//              .addFilterAfter(new CSRFHeaderFilter(), CsrfFilter.class)  // kendi filtresinden sonra bizim yazdığımız filtreyi eklesin
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers(HttpMethod.POST, "/api/v1/users2/register",
                                        "/api/v1/users2/login","/api/v1/users2/login2").permitAll()
                                .anyRequest().authenticated()


//                        .requestMatchers(HttpMethod.GET, "/api/v1/cars2").hasAuthority("ROLE_ADMIN")
//                    // BASIC AUT için application.pro 'da rollerde ADMIN yoksa FORBIDDEN hatası verir
//                        .requestMatchers(HttpMethod.POST, "/api/v1/cars2").hasAuthority("ROLE_ADMIN")
//                        .requestMatchers(HttpMethod.PUT, "/api/v1/cars2/id/{id}").hasAuthority("ROLE_ADMIN")
//                        .requestMatchers("/api/v1/users2/profile","/api/v1/users2/profile2","/api/v1/users2/profile3").authenticated()
//                        .requestMatchers("/api/v1/users2/profile4","/api/v1/users2/profile5","/api/v1/users2/profile6").authenticated()




                        //.anyRequest().authenticated()                        // her istek için yetki gerek
                        //.anyRequest().permitAll()                            // her istek i.in yetki gerekmez(Anasayfa vb.)
                        //.requestMatchers("api/v1/cars2").authenticated()     // sadece cars2 'deki POST metodu için yetki ister.
                        //.requestMatchers("api/v1/cars2/**").authenticated()  // cars2 ' deki tüm metodlar için yetki ister
                        //.requestMatchers("/api/v1/cars/**").hasRole("ADMIN") // sadece Admin rolündeki kişiler cars2 ' ye erişebilir
                        //.requestMatchers("/api/v1/cars/**").hasAuthority("ROLE_ADMIN")  // üsttekiyle aynı metottur farkı üstte "ROLE_" 'ü kendisi ekler
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                //.httpBasic(Customizer.withDefaults())  JWT kullanacağımız için httpBasici kapatıyoruz ve sessionı stateless yapıyoruz
                .build();
    }

    // AuthenticationProvider duplicated oldu bu yüzden onun yerine AuthenticationManager kullanıyoruz.
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        // httpden gelen objecti userDetailse ve passwordEncodera gönderir
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(myUserDetailsService)
                .passwordEncoder(passwordEncoder());

        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}








//    // Yine BASIC AUT için ayrı bir ilkel yöntem
//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user = User
//                .builder()
//                .username("user")
//                .password(passwordEncoder().encode("123"))
//                .roles("USER")
//                .build();
//
//        UserDetails admin = User
//                .builder()
//                .username("admin")
//                .password(passwordEncoder().encode("123"))
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(user, admin);
//    }