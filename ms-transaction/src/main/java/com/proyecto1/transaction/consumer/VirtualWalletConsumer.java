package com.proyecto1.transaction.consumer;

import java.math.BigDecimal;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.proyecto1.transaction.client.BuyBcClient;
import com.proyecto1.transaction.client.DebitCardClient;
import com.proyecto1.transaction.client.VirtualWalletClient;
import com.proyecto1.transaction.client.WalletBcClient;
import com.proyecto1.transaction.entity.BuyBootCoin;
import com.proyecto1.transaction.entity.Exchange;
import com.proyecto1.transaction.entity.Transaction;
import com.proyecto1.transaction.entity.VirtualWalletEvent;
import com.proyecto1.transaction.service.TransactionService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class VirtualWalletConsumer {
	
	@Autowired
	DebitCardClient debitCardClient;
	
	@Autowired
	BuyBcClient buyBcClient;
	
	@Autowired
	WalletBcClient walletBcClient;
	
	@Autowired
	VirtualWalletClient virtualWalletClient;
	
	@Autowired
	TransactionService transactionService;
	
	@KafkaListener(topics = {"virtual-wallet-events"})
	public void onMessage(ConsumerRecord<Integer, String> consumerRecord) {
		log.info("ConsumerRecord: {}", consumerRecord);
		VirtualWalletEvent vwEvent = new Gson().fromJson(consumerRecord.value(), VirtualWalletEvent.class);
		if(vwEvent.getCardNumberEmisor()!=null) {
			updateEmisor(vwEvent);
		}
		if(vwEvent.getCardNumberReceptor()!=null) {
			updateReceptor(vwEvent);
		} 
		
		
	}
	
	
	@KafkaListener(topics = {"exchange-rate-events"})
	public void onMessageExchangeRateWithBuyBootcoin(ConsumerRecord<Integer, String> consumerRecord) {
		log.info("ConsumerRecord: {}", consumerRecord);
		
		Exchange exchange = new Gson().fromJson(consumerRecord.value(), Exchange.class);
		BuyBootCoin buyBootCoin = exchange.getBuyBootCoin();
		
		
		Mono<BuyBootCoin> buyBootCoinUpdate = walletBcClient.getWalletBcById(buyBootCoin.getWalletId()).flatMap(wbc -> {
			wbc.getCelular();
			buyBootCoin.getModoDePago();
			
			if (buyBootCoin.getModoDePago().equalsIgnoreCase("CUENTA BANCARIA")) {
				return bankAccountValidation(buyBootCoin).flatMap(hasBankAccount -> {
					if (hasBankAccount) {
						// realizar el pago
						return updateReceptorAmount(buyBootCoin).collectList().flatMap(lstTrans -> {
							return Mono.just(buyBootCoin);
						});
						
					} 
				    return Mono.just(buyBootCoin);
					
					
				});
			}
			return Mono.just(buyBootCoin);
			/*
			if (buyBootCoin.getModoDePago().equalsIgnoreCase("YANKI")) {
				yankiValidation(buyBootCoin).flatMap(hasYanki -> {
					if (hasYanki) {
						// realizar el pago
					}
					return Mono.just(null);
				});
			}
			*/
			//return Mono.just(null);
			/*
			return transactionService.findByIdWithCustomer(buyBootCoin.getAccountIdReceptor())
					.filter(t -> t.getProduct().getTypeProduct() == 1 || t.getProduct().getTypeProduct() == 3)
					.hasElement()
					.flatMap(b -> {
						if (b) {
							// Actualizar los valores de las cuentas
							updateReceptorBc(buyBootCoin.getAccountIdReceptor(), buyBootCoin.getMontoSoles());
							return Mono.just(null);
						} else {
							// Update el estado a rechazado
							buyBootCoin.setState("Rechazado");
							return buyBcClient.updateBuyBootCoin(buyBootCoin);
							
						}
					});*/
		});
		
		
		buyBootCoinUpdate.subscribe(t -> log.info("Entro a funcion updateReceptorBootcoin valor {}", t.toString()));
		
	}
	
	private Flux<Transaction> updateReceptorAmount (BuyBootCoin buyBootCoin) {
		return transactionService.findAllWithDetail()
				.filter(trans -> trans.getCustomerId().equalsIgnoreCase(buyBootCoin.getAccountIdReceptor()))
				.collectList()
				.flatMapMany(trans -> {
					trans.sort((o1, o2) -> o1.getCreditCardAssociationDate().compareTo(o2.getCreditCardAssociationDate()));
					Transaction otrans = trans.stream().filter(t -> t.getProduct().getTypeProduct() == 1 || t.getProduct().getTypeProduct() == 2).findFirst().get();
					
					// seteamos valor en soles al vendedor
					otrans.setAvailableBalance(otrans.getAvailableBalance().add(buyBootCoin.getMontoSoles()));
					log.info("Receptor monto listo para updatear");
					return transactionService.update(otrans, otrans.getId());
				});
		
	}
	
	private Flux<Transaction> updateEmisorAmount (BuyBootCoin buyBootCoin) {
		return transactionService.findAllWithDetail()
				.filter(trans -> trans.getCustomerId().equalsIgnoreCase(buyBootCoin.getCustomerIdEmisor()))
				.collectList()
				.flatMapMany(trans -> {
					trans.sort((o1, o2) -> o1.getCreditCardAssociationDate().compareTo(o2.getCreditCardAssociationDate()));
					Transaction otrans = trans.stream().filter(t -> t.getProduct().getTypeProduct() == 1 || t.getProduct().getTypeProduct() == 2).findFirst().get();
					
					// seteamos valor en soles al emisor restandole el monto
					otrans.setAvailableBalance(otrans.getAvailableBalance().subtract(buyBootCoin.getMontoSoles()));
					log.info("Emisor monto listo para updatear {} -", buyBootCoin.getMontoSoles());
					return transactionService.update(otrans, otrans.getId());
				});
		
	}
	
	private Mono<Boolean> bankAccountValidation(BuyBootCoin buyBootCoin) {
		return transactionService.findAllWithDetail().filter(trans -> trans.getCustomerId().equalsIgnoreCase(buyBootCoin.getAccountIdReceptor()))
		.filter(trans -> trans.getProduct().getTypeProduct() == 1 || trans.getProduct().getTypeProduct() == 2)
		.hasElements();
	}
	
	private Mono<Boolean> yankiValidation (BuyBootCoin buyBootCoin) {
		return walletBcClient.getWalletBcById(buyBootCoin.getWalletId()).flatMap(walletBc -> {
			return virtualWalletClient.getVirtualWallets().filter(vw -> vw.getCellphone().equalsIgnoreCase(walletBc.getCelular()))
					.hasElements();
		});
	}
	
	private void updateReceptorBc(String accountIdReceptor, BigDecimal monto) {
		transactionService.findById(accountIdReceptor).flatMap(t -> {
			t.setAvailableBalance(t.getAvailableBalance().subtract(monto));
			return Mono.just(t);
		});
	}
	
	private void updateEmisor(VirtualWalletEvent vwEvent) {
		
		
		Flux<Transaction> tra = debitCardClient.getPrincipalDebitAccount(vwEvent.getCardNumberEmisor()).flatMap(debitCard -> {
			if (debitCard.getTrans().getAvailableBalance().compareTo(vwEvent.getAmount()) >= 0) {
				debitCard.getTrans().setAvailableBalance(debitCard.getTrans().getAvailableBalance().subtract(vwEvent.getAmount()));
				return transactionService.update(debitCard.getTrans(), debitCard.getTrans().getId());
			} else {
				return Mono.error(new Throwable());
			}
		});
		
		tra.subscribe(t -> log.info("Entro a funcion updateEmisor valor {}", t.toString()));
	}
	
	private void updateReceptor(VirtualWalletEvent vwEvent) {
		Flux<Transaction> tra = debitCardClient.getPrincipalDebitAccount(vwEvent.getCardNumberReceptor()).flatMap(debitCard -> {
				debitCard.getTrans().setAvailableBalance(vwEvent.getAmount().add(debitCard.getTrans().getAvailableBalance()));
				return transactionService.update(debitCard.getTrans(), debitCard.getTrans().getId());	
		});
		
		tra.subscribe(t -> log.info("Entro a funcion updateReceptor valor {}", t.toString()));
	}
}
