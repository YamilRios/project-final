package com.proyecto1.transaction.controller;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto1.transaction.entity.Transaction;
import com.proyecto1.transaction.service.TransactionService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private static final Logger log = LogManager.getLogger(TransactionController.class);
    @Autowired
    TransactionService transactionService;

    @GetMapping("/findAll")
    public Flux<Transaction> getTransaction(){
        log.info("Service call FindAll - transaction");
        return transactionService.findAll();
    }
    
    @GetMapping("/lastTenMovements")
    public Flux<Transaction> getLastTenMovements(){
        log.info("Service call lastTenMovements - transaction");
        return transactionService.lastTenMovements();
    }
    
    
    
    @GetMapping("/findAllWithDetail")
    public Flux<Transaction> getTransactions(){
        log.info("Service call findAllWithDetail - transaction");
        return transactionService.findAllWithDetail();
    }

    @GetMapping("/find/{id}")
    public Mono<Transaction> getTransaction(@PathVariable String id){
        log.info("Service call FindById - transaction");
        return transactionService.findById(id);
    }
    
    @GetMapping("/findByIdWithCustomer/{id}")
    public Mono<Transaction> getTransactionWithCustomer(@PathVariable String id){
        log.info("Service call findWithCustomer - transaction");
        return transactionService.findByIdWithCustomer(id);
    }
    @PostMapping("/create")
    public Mono<Transaction> createTransaction(@RequestBody Transaction t){
        log.info("Service call Create - transaction");
        return transactionService.save(t);
    }

    @PutMapping("/update/{id}")
    public Mono<Transaction> updateTransaction(@RequestBody Transaction t, @PathVariable String id){
        log.info("Service call Update - transaction");
        return transactionService.update(t,id);
    }

    @DeleteMapping("/delete/{id}")
    public Mono<Transaction> deleteTransaction(@PathVariable String id){
        log.info("Service call Delete - transaction");
        return transactionService.delete(id);
    }
    
    @GetMapping("/findAllByCustomerId/{id}")
    public Flux<Transaction> getTransactionsByCustomerId(@PathVariable String id){
        log.info("Service call findAllByCustomerId - transaction");
        return transactionService.findAllByCustomerId(id);
    }
    
    @PatchMapping("/patch/{id}")
    public Mono<Transaction> updateCreditAsociation(@RequestBody Transaction t, @PathVariable String id){
        log.info("Service call Patch - transaction");
        Optional<Mono<Transaction>> oTransaction = Optional.ofNullable(transactionService.findById(id));
        
        if(oTransaction.isPresent()) {
        	Mono<Transaction> o = oTransaction.get();
        	return o.flatMap(trans -> {
        		trans.setCreditCardAssociationDate(t.getCreditCardAssociationDate());
        		return transactionService.update(trans,id);
        	});
        } else {
        	return Mono.error(new Throwable("No existe la transaccion indicada"));
        }
        
        
    }
}
