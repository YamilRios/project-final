package com.example.demo.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.demo.entity.BuyBootCoin;
import com.example.demo.entity.Exchange;
import com.example.demo.service.ExchangeService;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ExchangeRateConsumer {
	
	@Autowired
	ExchangeService exchangeService;
	
	@KafkaListener(topics = {"buy-bootcoin-events"})
	public void onMessageBuyBc(ConsumerRecord<Integer, String> consumerRecord) {
		log.info("ConsumerRecord: {}", consumerRecord);
		BuyBootCoin buyBootCoin = new Gson().fromJson(consumerRecord.value(), BuyBootCoin.class);
		
		Exchange ex = new Exchange();
		ex.setMontoSoles(buyBootCoin.getMontoSoles());
		ex.setBuyBootCoinId(buyBootCoin.getId());
		
		exchangeService.solesToBootcoin(ex).flatMap(exf -> {
			exf.setBuyBootCoin(buyBootCoin);
			return Mono.just(exf);
		});
	}
	
}
