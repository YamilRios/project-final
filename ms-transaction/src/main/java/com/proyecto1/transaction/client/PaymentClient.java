package com.proyecto1.transaction.client;


import com.proyecto1.transaction.entity.Payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
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
