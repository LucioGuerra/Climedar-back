package com.climedar.person_sv.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers("/graphiql/**").permitAll()
                        .requestMatchers("/graphql/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .cors(withDefaults())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
                .sessionManagement((session) -> session.sessionCreationPolicy(STATELESS))
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return new JwtDecoder() {
            @Override
            public Jwt decode(String token) throws JwtException {
                return null;
            }
        };
    }
}
