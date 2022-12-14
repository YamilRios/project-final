package com.proyecto1.transaction.client;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.proyecto1.transaction.entity.Customer;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CustomerClient {
	
    private final ReactiveCircuitBreakerFactory reactiveCircuitBreakerFactory;

    @Value("${config.customer.endpoint}")
    String customerPath;
	
	@Autowired
	WebClient.Builder client;
    
    public Mono<Customer> getCustomer(String id){
        return client.build().get()
                .uri(customerPath+"/find/"+id)
                .retrieve()
                .bodyToMono(Customer.class)
                .transform( it -> {
                    ReactiveCircuitBreaker rcb = reactiveCircuitBreakerFactory.create("customer-service-client");

                    Customer nCustomer = new Customer();
                    nCustomer.setId(ObjectId.get().toString());
                    nCustomer.setName("EmpleadoPrueba");
                    nCustomer.setLastName("prueba");
                    nCustomer.setDocNumber("63748512");
                    nCustomer.setTypeCustomer(1);
                    nCustomer.setDescTypeCustomer("PERSONAL");return rcb.run(it, throwable -> Mono.just(nCustomer));
                });
      };
}
