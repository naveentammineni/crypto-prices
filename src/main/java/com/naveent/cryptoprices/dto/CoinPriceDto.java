package com.naveent.cryptoprices.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoinPriceDto {

    String symbol;
    double currentPrice;
    int marketCapRank;
    String image;
    double marketCap;

}
