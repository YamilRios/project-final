package com.proyecto1.signatory.client;

import com.proyecto1.signatory.entity.Customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class CustomerClient {

	@Value("${config.customer.endpoint}")
    String customerPath;
	
	@Autowired
	WebClient.Builder client;

    public Mono<Customer> getCustomer(String id){
        return client.build().get()
                .uri(uriBuilder -> uriBuilder
                        .path(customerPath+"/find/{id}")
                        .build(id)
                )
                .retrieve()
                .bodyToMono(Customer.class);
    };
}
