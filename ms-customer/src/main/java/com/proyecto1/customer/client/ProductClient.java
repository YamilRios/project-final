package com.proyecto1.customer.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.proyecto1.customer.entity.Product;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ProductClient {
	
	@Value("${config.product.endpoint}")
	String path;
	
	@Autowired
	WebClient.Builder client;

    public Mono<Product> getProduct(String id){
    	return client.build().get()
                .uri(path+"/find/"+id)
                /*.uri(uriBuilder -> uriBuilder
                        .path("/find/{id}")
                        .build(id))*/

                .retrieve()
                .bodyToMono(Product.class);
    }
    
    public Flux<Product> getProducts(){
    	return client.build().get()
                .uri(path+"/findAll")
                .retrieve()
                .bodyToFlux(Product.class);
    }
}
