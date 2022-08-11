package com.proyecto1.transaction.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.proyecto1.transaction.entity.Product;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ProductClient {
	

	@Value("${config.product.endpoint}")
    String pathProduct;
	
	@Autowired
	WebClient.Builder product;
	
    public Mono<Product> getProduct(String id){
    	return product.build().get()
                .uri(pathProduct+"/find/"+id)
                /*.uri(uriBuilder -> uriBuilder
                        .path("/find/{id}")
                        .build(id))*/

                .retrieve()
                .bodyToMono(Product.class);
    }
    
    public Flux<Product> getProducts(){
    	return product.build().get()
                .uri("/findAll")
                .retrieve()
                .bodyToFlux(Product.class);
    }
}
