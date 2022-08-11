package com.proyecto1.customer.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.proyecto1.customer.entity.Signatory;

import reactor.core.publisher.Flux;
@Component
public class SignatoryClient {
	
	@Value("${config.signatory.endpoint}")
	String path;
	
	@Autowired
	WebClient.Builder client;

    public Flux<Signatory> getSignatory(){
        return client.build().get()
                .uri(path+"/findAll")
                .retrieve()
                .bodyToFlux(Signatory.class);
    }
}
