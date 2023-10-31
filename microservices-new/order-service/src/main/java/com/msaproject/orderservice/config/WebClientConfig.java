package com.msaproject.orderservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean //bean will be created with the method name
    @LoadBalanced //client side load balancing
    public WebClient.Builder webClientBuilder(){
        return WebClient.builder();
    }

}
