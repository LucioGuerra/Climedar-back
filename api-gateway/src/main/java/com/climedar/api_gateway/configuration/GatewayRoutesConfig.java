package com.climedar.api_gateway.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class GatewayRoutesConfig {

    @Value("${EUREKA_URL}")
    private String eurekaUrl;


    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("apollo-federation", r -> r.path("/graphql/**")
                        .filters(GatewayFilterSpec::tokenRelay)
                        .uri("lb://apollo-federation"))
                .route("payment-sv", r -> r.path("/api/**")
                        .filters(GatewayFilterSpec::tokenRelay)
                        .uri("lb://payment-sv"))
                .route("eureka-sv", r -> r.path("/eureka/**")
                        .filters(GatewayFilterSpec::tokenRelay)
                        .uri("http://" + eurekaUrl))
                .build();
    }
}
