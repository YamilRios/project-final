package com.proyecto1.payment.client;

import com.proyecto1.payment.entity.Transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
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
}
