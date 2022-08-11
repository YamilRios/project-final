package com.proyecto1.product.client;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.proyecto1.product.entity.Withdrawal;

import reactor.core.publisher.Flux;

@Component
public class WithDrawalClient {
	
	@Value("${config.withdrawal.endpoint}")
    String withdrawal;
	
	@Autowired
	WebClient.Builder client;

    public Flux<Withdrawal> getWithDrawal(){
        return client.build().get()
                .uri(withdrawal+"/findAll")
                .retrieve()
                .bodyToFlux(Withdrawal.class);
    }
}
