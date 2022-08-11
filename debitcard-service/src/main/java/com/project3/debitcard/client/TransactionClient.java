package com.project3.debitcard.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.project3.debitcard.entity.Transaction;

import reactor.core.publisher.Flux;

@Component
public class TransactionClient {
	
	@Value("${config.transaction.client}")
	String path;
	
	@Autowired
	WebClient.Builder client;

    public Flux<Transaction> findAllWithDetail(){
        return client.build().get()
                .uri(path+"/findAllWithDetail")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Transaction.class);
    };
}
