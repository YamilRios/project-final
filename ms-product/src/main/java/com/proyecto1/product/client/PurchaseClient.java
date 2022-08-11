package com.proyecto1.product.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.proyecto1.product.entity.Purchase;

import reactor.core.publisher.Flux;

@Component
public class PurchaseClient {
	
	@Value("${config.purchase.endpoint}")
    String purchaseProduct;
	
	@Autowired
	WebClient.Builder client;

    public Flux<Purchase> getPurchase(){
        return client.build().get()
                .uri(purchaseProduct+"/findAll")
                .retrieve()
                .bodyToFlux(Purchase.class);
    }
}
