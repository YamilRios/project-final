package com.example.demo.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.demo.entity.BuyBootCoin;
import com.example.demo.entity.Exchange;
import com.example.demo.producer.ExchangeRateProducer;
import com.example.demo.service.ExchangeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ExchangeRateConsumer {
	
	@Autowired
	ExchangeService exchangeService;
	
	@Autowired
	ExchangeRateProducer exchangeRateProducer;
	
	@KafkaListener(topics = {"buy-bootcoin-events"})
	public void onMessageBuyBc(ConsumerRecord<Integer, String> consumerRecord) {
		log.info("ConsumerRecord: {}", consumerRecord);
		BuyBootCoin buyBootCoin = new Gson().fromJson(consumerRecord.value(), BuyBootCoin.class);
		
		Exchange ex = new Exchange();
		ex.setMontoSoles(buyBootCoin.getMontoSoles());
		ex.setBuyBootCoinId(buyBootCoin.getId());
		
		Mono<Exchange> exchangeRateToSend = exchangeService.solesToBootcoin(ex).flatMap(exf -> {
			exf.setBuyBootCoin(buyBootCoin);
			try {
				exchangeRateProducer.sendExchangeRateEvent(exf);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			return Mono.just(exf);
		});
		
		// Llamar a producer una ves terminado lo de arriba
		
		exchangeRateToSend.subscribe(t -> log.info("Entro a funcion sendExchangeRateEvent "));
		
	}
	
}
