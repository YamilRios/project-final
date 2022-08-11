package com.proyecto1.transaction.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.proyecto1.transaction.entity.VirtualWallet;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class VirtualWalletClient {

	@Value("${config.virtualwalletservice.endpoint}")
    String virtualWallet;
	
	@Autowired
	WebClient.Builder client;
	
    public Flux<VirtualWallet> getVirtualWallets(){
        return client.build().get()
                .uri(virtualWallet+"/findAll")
                .retrieve()
                .bodyToFlux(VirtualWallet.class);
    }
    
    public Mono<VirtualWallet> getVirtualWalletById(String id){
    	return client.build().get()
                .uri(virtualWallet+"/find/"+id)
                .retrieve()
                .bodyToMono(VirtualWallet.class);
    }
    
    public Mono<VirtualWallet> updateVirtualWallet(VirtualWallet virtualWallet){
        return client.build().put()
                .uri(uriBuilder -> uriBuilder
                        .path(virtualWallet+"/update/{id}")
                        .build(virtualWallet.getId())
                )
                .bodyValue(virtualWallet)
                .retrieve()
                .bodyToMono(VirtualWallet.class);
    };
}
