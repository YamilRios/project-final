package com.proyecto1.transaction.client;

import com.proyecto1.transaction.entity.Signatory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class SignatoryClient {

    @Value("${config.signatory.endpoint}")
    String signatoryProduct;
	
	@Autowired
	WebClient.Builder client;
    
    public Flux<Signatory> getSignatory(){
        return client.build().get()
                .uri(signatoryProduct+"/findAll")
                .retrieve()
                .bodyToFlux(Signatory.class);
    }
}
