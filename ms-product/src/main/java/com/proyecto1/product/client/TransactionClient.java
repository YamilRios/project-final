package com.proyecto1.product.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.proyecto1.product.entity.Transaction;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class TransactionClient {
	
	@Value("${config.transaction.endpoint}")
	String path;
	
	@Autowired
	WebClient.Builder client;

    public Mono<Transaction> getTransactionWithDetails(String id){
        return client.build().get()
                .uri(uriBuilder -> uriBuilder
                        .path(path+"/findByIdWithCustomer/{id}")
                        .build(id)
                )
                .retrieve()
                .bodyToMono(Transaction.class);
    };
    
    public Mono<Transaction> updateTransaction(Transaction transaction){
        return client.build().put()
                .uri(uriBuilder -> uriBuilder
                        .path(path+"/update/{id}")
                        .build(transaction.getId())
                )
                .bodyValue(transaction)
                .retrieve()
                .bodyToMono(Transaction.class);
    };
    
    
    public Flux<Transaction> findAll(){
        return client.build().get()
                .uri(path+"/findAll")
                .retrieve()
                .bodyToFlux(Transaction.class);
    };
    
    public Flux<Transaction> findAllWithDetail(){
        return client.build().get()
                .uri(path+"/findAllWithDetail")
                .retrieve()
                .bodyToFlux(Transaction.class);
    };
}
