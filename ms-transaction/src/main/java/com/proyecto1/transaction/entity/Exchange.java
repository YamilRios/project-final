package com.proyecto1.transaction.entity;

import java.math.BigDecimal;

import org.springframework.data.annotation.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Exchange {


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
