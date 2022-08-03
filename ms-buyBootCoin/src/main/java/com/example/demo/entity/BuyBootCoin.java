package com.example.demo.entity;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Document(collection = "schema_buy.buyBootCoin")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuyBootCoin {

    @Id
    private String id;
    private String walletId;
    private String customerIdEmisor;
    private String accountIdReceptor;
    private BigDecimal montoSoles;
    private String state;
    private String modoDePago;
    
    private Integer buyBootCoinId;
  

}
