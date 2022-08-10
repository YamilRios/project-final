package com.proyecto1.customer.controller;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.proyecto1.customer.dto.CustomerDTO;
import com.proyecto1.customer.entity.Customer;
import com.proyecto1.customer.entity.Transaction;
import com.proyecto1.customer.service.CustomerService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private static final Logger log = LogManager.getLogger(CustomerController.class);
    @Autowired
    private CustomerService customerService;

    @GetMapping("/summaryCustomerByProduct")
    public Flux<Transaction> summaryCustomerByProduct(){
        log.info("Service call summaryCustomerByProduct");
        return customerService.summaryCustomerByProduct();
    }
    
    @GetMapping("/findAll")
    public Mono<ResponseEntity<Flux<Customer>>> getCustomers(){
        log.info("Service call findAll - customer");
        return Mono.just(ResponseEntity.ok()
        		.contentType(MediaType.APPLICATION_JSON)
        		.body(customerService.findAll()));
    }

    @GetMapping("/find/{id}")
    public Mono<Customer> getCustomer(@PathVariable String id){
        log.info("Service call findById - customer");
        return customerService.findById(id);
    }

    @PostMapping("/create")
    public Mono<ResponseEntity<Map<String, Object>>> createCustomer(@Valid @RequestBody Mono<CustomerDTO> monoCustomer){
        log.info("Service call create - customer");
 
        Map<String, Object> response = new HashMap<>();
        
        return monoCustomer.flatMap(c -> {
        	return customerService.create(c).map(cs -> {
        		response.put("customer", cs);
        		response.put("mensjae", "Customer creado exitosamente");
        		return ResponseEntity.created(URI.create("/customer/create/"+cs.getId()))
            			.contentType(MediaType.APPLICATION_JSON)
            			.body(response);
        	});
        }).onErrorResume(t -> {
        	return Mono.just(t).cast(WebExchangeBindException.class).flatMap(we -> Mono.just(we.getFieldErrors()))
        	.flatMapMany(Flux::fromIterable)
        	.map(error -> "El campo " + error.getField() + " "+ error.getDefaultMessage())
        	.collectList()
        	.flatMap(list -> {
        		response.put("errores", list);
        		response.put("timestamp", new Date());
        		response.put("status", HttpStatus.BAD_REQUEST.value());
        		return Mono.just(ResponseEntity.badRequest().body(response));
        	});
        });
        
        
    }

    @PutMapping("/update/{id}")
    public Mono<Customer> updateCustomer(@RequestBody CustomerDTO c, @PathVariable String id){
        log.info("Service call update - customer");
        return customerService.update(c,id);
    }

    @DeleteMapping("/delete/{id}")
    public Mono<Customer> deleteCustomer(@PathVariable String id){
        log.info("Service call delete - customer");
                return customerService.delete(id);
    }


}
