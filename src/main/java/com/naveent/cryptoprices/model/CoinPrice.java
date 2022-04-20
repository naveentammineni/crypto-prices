package com.naveent.cryptoprices.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CoinPrice {

    @Id
    int marketCapRank;
    String symbol;
    double currentPrice;
    String image;
    double marketCap;

}
