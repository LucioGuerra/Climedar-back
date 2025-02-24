package com.climedar.library;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        String jwt = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();

        if (jwt != null) {
            template.header("Authorization", "Bearer " + jwt);
        }
    }
}
