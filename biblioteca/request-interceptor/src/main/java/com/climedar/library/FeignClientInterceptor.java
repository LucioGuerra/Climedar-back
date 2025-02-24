package com.climedar.library;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        try {
            String jwt = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();

            if (jwt != null) {
                template.header("Authorization", "Bearer " + jwt);
            }
        }catch (Exception e){
            System.out.println("Error in FeignClientInterceptor");
        }

    }
}
