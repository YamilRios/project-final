package com.proyecto1.transaction.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.proyecto1.transaction.entity.DebitCard;
import com.proyecto1.transaction.entity.VirtualWalletEvent;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class DebitCardClient {

	@Value("${config.debitcard.endpoint}")
    String debitcardPath;
	
	@Autowired
	WebClient.Builder client;
	
    public Flux<DebitCard> getDebitCards(){
        return client.build().get()
                .uri(debitcardPath+"/findAll")
                .retrieve()
                .bodyToFlux(DebitCard.class);
    }
    
    public Mono<DebitCard> getDebitCardByTransactionId(String id){
    	return client.build().get()
                .uri(debitcardPath+"/findByTransactionId/"+id)
                .retrieve()
                .bodyToMono(DebitCard.class);
    }
    
    public Flux<DebitCard> getPrincipalDebitAccount(String cardNumber){
        return client.build().get()
        		.uri(uriBuilder -> uriBuilder
                        .path(debitcardPath+"/principalDebitAccount/{id}")
                        .build(cardNumber)
                )
                .retrieve()
                .bodyToFlux(DebitCard.class);
    }
}
