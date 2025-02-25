package com.climedar.library;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            System.out.println("Authentication found: " + auth.getName());
            Object credentials = auth.getCredentials();
            if (credentials != null) {
                Jwt jwt = (Jwt) credentials;
                String jwtValue = jwt.getTokenValue();
                System.out.println("JWT found in credentials: " + jwtValue);
                template.header("Authorization", "Bearer " + jwtValue);
                System.out.println("Added Authorization header to FeignClient request");
            } else {
                System.out.println("No JWT found in credentials");
            }
        } else {
            System.out.println("No Authentication present in the SecurityContext");
        }
    }
}
