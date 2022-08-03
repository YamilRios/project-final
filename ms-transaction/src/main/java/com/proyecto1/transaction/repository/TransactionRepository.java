package com.proyecto1.transaction.repository;

import com.proyecto1.transaction.entity.Transaction;

import reactor.core.publisher.Flux;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends ReactiveCrudRepository<Transaction, String> {
	
	Flux<Transaction> findAllByCustomerId(String customerId);
}
