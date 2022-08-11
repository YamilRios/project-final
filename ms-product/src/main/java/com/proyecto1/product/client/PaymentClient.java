package com.proyecto1.product.client;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.proyecto1.product.entity.Payment;

import reactor.core.publisher.Flux;

@Component
public class PaymentClient {
	
	@Value("${config.payment.endpoint}")
    String paymentPath;
	
	@Autowired
	WebClient.Builder client;

    public Flux<Payment> getPayment(){
        return client.build().get()
                .uri(paymentPath+"/findAll")
                .retrieve()
                .bodyToFlux(Payment.class);
    }
}
