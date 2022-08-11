package com.proyecto1.transaction.config;

import java.time.Duration;

import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.web.reactive.function.client.WebClient;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

@Configuration
@EnableKafka
public class TransactionConfig {
	
	
	
    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> customerCustomizer(){
        return factory -> {
            factory.configure( builder -> builder
                    .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(2)).build())
                    .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults()),"ms-customers-client");
        };
    };
    
    @Bean
    @LoadBalanced
    public WebClient.Builder getWebClient() {
    	return WebClient.builder();
    }
    
}