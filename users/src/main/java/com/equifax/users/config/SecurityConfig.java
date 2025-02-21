package com.equifax.users.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {


    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Configures the Spring Security filter chain for the application. This configuration includes:
     * - Permitting access to specific public endpoints such as login, sign-up, Swagger documentation, H2 console, and actuator endpoints.
     * - Requiring authentication for all other endpoints.
     * - Disabling Cross-Site Request Forgery (CSRF) protection.
     * - Configuring headers to allow frames from the same origin (useful for H2 console).
     * - Adding a custom JWT authentication filter for processing JWT tokens before the standard authentication filter.
     *
     * @param http the HttpSecurity object used to configure security settings for HTTP requests
     * @return a configured {@code SecurityFilterChain} instance
     * @throws Exception if there's an issue in configuring the security filter chain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/users/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/users/import").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    /**
     * Provides a BCryptPasswordEncoder bean to be used for password encoding in the application.
     *
     * @return an instance of BCryptPasswordEncoder
     */
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:4200")); // Permite solo origen frontend
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Métodos permitidos
        configuration.setAllowCredentials(true); // Si envías cookies o encabezados de autenticación
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type")); // Encabezados permitidos
        configuration.setExposedHeaders(List.of("Authorization")); // Exposición opcional

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}
