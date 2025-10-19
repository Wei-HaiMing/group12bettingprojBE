package com.grouptwelve.grouptwelveBE.config;

import com.grouptwelve.grouptwelveBE.security.OAuth2LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource))  // Enable CORS
            .csrf(csrf -> csrf.disable())  // Disable CSRF for API testing
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login**", "/error", "/webjars/**").permitAll()
                .requestMatchers("/api/**").permitAll()  // Allow all API endpoints without auth (for now)
                .requestMatchers("/auth/**").permitAll()  // Allow auth endpoints
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .successHandler(oAuth2LoginSuccessHandler)
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .permitAll()
            );
        
        return http.build();
    }
}
