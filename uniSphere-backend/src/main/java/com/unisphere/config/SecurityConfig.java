package com.unisphere.config;

import com.unisphere.security.AuthEntryPointJwt;
import com.unisphere.security.AuthTokenFilter;
import com.unisphere.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(unauthorizedHandler)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // ===== PUBLIC ENDPOINTS (No authentication required) =====

                        // Authentication endpoints
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()

                        // Test endpoints
                        .requestMatchers("/test/**").permitAll()
                        .requestMatchers("/api/test/**").permitAll()

                        // University public endpoints
                        .requestMatchers("/universities").permitAll()                    // GET all universities
                        .requestMatchers("/universities/*").permitAll()                  // GET university by ID
                        .requestMatchers("/universities/verify/**").permitAll()          // Verification endpoints

                        // Community public endpoints
                        .requestMatchers("/communities/university/*").permitAll()        // GET university communities
                        .requestMatchers("/communities/*").permitAll()                   // GET community by ID
                        .requestMatchers("/communities/search").permitAll()              // Search communities
                        .requestMatchers("/communities/trending").permitAll()            // Trending communities

                        // Error handling
                        .requestMatchers("/error").permitAll()

                        // ===== PROTECTED ENDPOINTS (Require authentication) =====

                        // University protected endpoints
                        .requestMatchers("/universities/my-universities").authenticated() // User's universities
                        .requestMatchers("/universities/*/join").authenticated()         // Join university

                        // Community protected endpoints
                        .requestMatchers("/communities").authenticated()                 // POST create community
                        .requestMatchers("/communities/*/join").authenticated()          // POST join community
                        .requestMatchers("/communities/*/leave").authenticated()         // POST leave community
                        .requestMatchers("/communities/my-communities").authenticated()  // GET user's communities
                        .requestMatchers("/communities/*/members").authenticated()       // GET community members
                        .requestMatchers("/communities/*/members/role").authenticated()  // PUT update member role
                        .requestMatchers("/communities/*").authenticated()               // PUT/DELETE community (update/delete)

                        // ===== ADMIN ONLY ENDPOINTS =====

                        // University admin endpoints
                        .requestMatchers("/universities").hasRole("ADMIN")               // POST create university
                        .requestMatchers("/universities/*").hasRole("ADMIN")             // PUT/DELETE university

                        // ===== FALLBACK =====
                        .anyRequest().authenticated() // All other endpoints require authentication
                );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Allow both Vite dev server (5173) and React default port (3000)
        configuration.setAllowedOriginPatterns(List.of("http://localhost:5173", "http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Auth-Token", "Content-Type", "X-Requested-With", "Accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        configuration.setExposedHeaders(List.of("X-Auth-Token"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L); // 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}