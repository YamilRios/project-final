package com.example.demo.entity;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "schema_ex.exchange")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Exchange {

    @Id
    private String id;
    private BigDecimal tazaCambio;

    @Transient
    private String customerId;
    @Transient
    private BigDecimal montoSoles;
    @Transient
    private BigDecimal montoBootCoin;
    @Transient
    private String buyBootCoinId;
    @Transient
    private BuyBootCoin buyBootCoin;
    @Transient
    private Integer buyBootCoinIntegerId;
}
