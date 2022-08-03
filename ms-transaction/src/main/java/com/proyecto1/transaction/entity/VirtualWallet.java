package com.proyecto1.transaction.entity;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VirtualWallet {
	
	private String id;
	private String dni;
	private String cellphone;
	private String operation;
	private BigDecimal amount;
	private String cardNumberEmisor;
	private String cardNumberReceptor;
	// customerid
}
