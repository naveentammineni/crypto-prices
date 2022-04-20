package com.naveent.cryptoprices.service;

import com.naveent.cryptoprices.exceptions.CoinRankNotFoundException;
import com.naveent.cryptoprices.model.CoinPrice;
import com.naveent.cryptoprices.model.CoinPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CryptoPricesService {

    @Autowired
    private CoinPriceRepository coinPriceRepository;

    public List<CoinPrice> list() {
        return coinPriceRepository.findAll();
    }

    public void create(CoinPrice coinPrice) {
        coinPriceRepository.save(coinPrice);
    }

    public CoinPrice read(int marketCapRank) {
        return coinPriceRepository.findById(marketCapRank)
                .orElseThrow(() -> new CoinRankNotFoundException("CoinPrice for rank" + marketCapRank + " is not found"));
    }

    public void update(CoinPrice coinPrice) {
        if (coinPriceRepository.findById(coinPrice.getMarketCapRank()).isPresent()) {
            coinPriceRepository.save(coinPrice);
        } else {
            throw new CoinRankNotFoundException("CoinPrice for rank" + coinPrice.getMarketCapRank() + " is not found");
        }
    }

    public void delete(int marketCapRank) {
        if (coinPriceRepository.findById(marketCapRank).isPresent()) {
            coinPriceRepository.deleteById(marketCapRank);
        } else {
            throw new CoinRankNotFoundException("CoinPrice for rank" + marketCapRank + " is not found");
        }

    }

}
