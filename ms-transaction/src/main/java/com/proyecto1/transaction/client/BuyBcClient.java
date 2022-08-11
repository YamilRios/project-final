package com.proyecto1.transaction.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.proyecto1.transaction.entity.BuyBootCoin;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class BuyBcClient {
	
	@Value("${config.buybootcoin.endpoint}")
    String buyBcClientPath;
	
	@Autowired
	WebClient.Builder client;

    public Flux<BuyBootCoin> getBuyBootcoins(){
        return client.build().get()
                .uri(buyBcClientPath+"/findAll")
                .retrieve()
                .bodyToFlux(BuyBootCoin.class);
    }
    
    public Mono<BuyBootCoin> getWalletBcById(String id){
    	return client.build().get()
                .uri(buyBcClientPath+"/find/"+id)
                .retrieve()
                .bodyToMono(BuyBootCoin.class);
    }
    
    public Mono<BuyBootCoin> updateBuyBootCoin(BuyBootCoin buyBootCoin){
        return client.build().put()
                .uri(uriBuilder -> uriBuilder
                        .path(buyBcClientPath+"/update/{id}")
                        .build(buyBootCoin.getId())
                )
                .bodyValue(buyBootCoin)
                .retrieve()
                .bodyToMono(BuyBootCoin.class);
    };
}
