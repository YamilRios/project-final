package com.proyecto1.withdrawal.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.proyecto1.withdrawal.entity.DebitCard;

import reactor.core.publisher.Flux;

@Component
public class DebitCardClient {
	@Value("${config.debitcard.endpoint}")
    String debitcardPath;
	
	@Autowired
	WebClient.Builder client;

    public Flux<DebitCard> getAccountDetailByDebitCard(String id){
        return client.build().get()
                .uri(uriBuilder -> uriBuilder
                        .path(debitcardPath+"/accountDetail/{id}")
                        .build(id)
                )
                .retrieve()
                .bodyToFlux(DebitCard.class);
    }
}
