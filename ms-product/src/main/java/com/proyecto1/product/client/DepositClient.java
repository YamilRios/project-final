package com.proyecto1.product.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.proyecto1.product.entity.Deposit;

import reactor.core.publisher.Flux;

@Component
public class DepositClient {
	
	@Value("${config.deposit.endpoint}")
    String depositPath;
	
	@Autowired
	WebClient.Builder client;

    public Flux<Deposit> getDeposit(){
        return client.build().get()
                .uri(depositPath+"/findAll")
                .retrieve()
                .bodyToFlux(Deposit.class);
    }
}
