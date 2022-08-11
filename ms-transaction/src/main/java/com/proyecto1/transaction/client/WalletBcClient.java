package com.proyecto1.transaction.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.proyecto1.transaction.entity.WalletBc;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class WalletBcClient {
	

	@Value("${config.walletbc.endpoint}")
    String walletBc;
	
	@Autowired
	WebClient.Builder client;
	
    public Flux<WalletBc> getWalletBcs(){
        return client.build().get()
                .uri(walletBc+"/findAll")
                .retrieve()
                .bodyToFlux(WalletBc.class);
    }
    
    public Mono<WalletBc> getWalletBcById(String id){
    	return client.build().get()
                .uri(walletBc+"/find/"+id)
                .retrieve()
                .bodyToMono(WalletBc.class);
    }
    
 
}
