package com.proyecto1.product.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.proyecto1.product.entity.Customer;

import reactor.core.publisher.Mono;

@Component
public class CustomerClient {
    
    @Value("${config.customer.endpoint}")
    String path;
	
	@Autowired
	WebClient.Builder client;

    public Mono<Customer> getCustomer(String id){
        return client.build().get()
                .uri(uriBuilder -> uriBuilder
                        .path(path+"/find/{id}")
                        .build(id)
                )
                .retrieve()
                .bodyToMono(Customer.class);
    };
}
