package com.proyecto1.customer.client;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.proyecto1.customer.entity.Payment;

import reactor.core.publisher.Flux;

@Component
public class PaymentClient {
    
    @Value("${config.payment.endpoint}")
	String path;
	
	@Autowired
	WebClient.Builder client;

    public Flux<Payment> getPayment(){
        return client.build().get()
                .uri(path+"/findAll")
                .retrieve()
                .bodyToFlux(Payment.class);
    }
}
