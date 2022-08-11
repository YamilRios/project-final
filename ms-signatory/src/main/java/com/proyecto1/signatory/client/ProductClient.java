package com.proyecto1.signatory.client;

import com.proyecto1.signatory.entity.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ProductClient {
    
	@Value("${config.product.endpoint}")
    String pathProduct;
	
	@Autowired
	WebClient.Builder product;

    public Mono<Product> getProduct(String id){
        return product.build().get()
                .uri(uriBuilder -> uriBuilder
                        .path(pathProduct+"/find/{id}")
                        .build(id)
                )
                .retrieve()
                .bodyToMono(Product.class);
    };
}
