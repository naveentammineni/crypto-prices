package com.naveent.cryptoprices.api;

import com.naveent.cryptoprices.dto.AlternateResponse;
import com.naveent.cryptoprices.dto.CoinPriceDto;
import com.naveent.cryptoprices.model.CoinPrice;
import com.naveent.cryptoprices.model.CoinPriceRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.util.Arrays;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class CryptoPricesControllerTests {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    CoinPriceRepository coinPriceRepository;

    private URI createListUrl(UriBuilder uriBuilder) {
        uriBuilder = uriBuilder.path("/prices/");
        return uriBuilder.build();
    }

    private URI createGetUrl(UriBuilder uriBuilder, int marketCapRank) {
        uriBuilder = uriBuilder.path("/prices/" + marketCapRank);
        return uriBuilder.build();
    }

    private URI createDeleteUrl(UriBuilder uriBuilder, int marketCapRank) {
        uriBuilder = uriBuilder.path("/prices/" + marketCapRank);
        return uriBuilder.build();
    }

    CoinPrice cp1 = CoinPrice.builder()
            .symbol("BTC")
            .image("https://assets.coingecko.com/coins/images/1/large/bitcoin.png?1547033579")
            .marketCapRank(1)
            .currentPrice(41333)
            .marketCap(786600187833L)
            .build();

    CoinPrice cp2 = CoinPrice.builder()
            .symbol("ETH")
            .image("https://assets.coingecko.com/coins/images/279/large/ethereum.png?1595348880")
            .marketCapRank(2)
            .currentPrice(3086.59)
            .marketCap(371783974462L)
            .build();

    @BeforeEach
    void setup() {
        coinPriceRepository.save(cp1);
        coinPriceRepository.save(cp2);
    }

    @Test
    @DisplayName("Test to get all coins, success.")
    void testListAllCoins() {

        webTestClient.get()
                .uri(this::createListUrl)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0]").isEqualTo(cp1)
                .jsonPath("$[1]").isEqualTo(cp2);

    }

    @Test
    @DisplayName("Test to get a crypto price by rank, success.")
    void testGetCryptoPriceByRank() {

        webTestClient.get()
                .uri(uriBuilder -> createGetUrl(uriBuilder, 1))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(CoinPriceDto.class)
                .consumeWith(coinPrice -> {
                    Assertions.assertThat(coinPrice).isNotNull();
                    Assertions.assertThat(coinPrice.getResponseBody()).isNotNull();
                    Assertions.assertThat(coinPrice.getResponseBody().getCurrentPrice()).isEqualTo(cp1.getCurrentPrice());
                    Assertions.assertThat(coinPrice.getResponseBody().getMarketCap()).isEqualTo(cp1.getMarketCap());
                    Assertions.assertThat(coinPrice.getResponseBody().getImage()).isEqualTo(cp1.getImage());
                    Assertions.assertThat(coinPrice.getResponseBody().getSymbol()).isEqualTo(cp1.getSymbol());
                });

    }

    @Test
    @DisplayName("Test to get a crypto price by rank, error.")
    void testGetCryptoPriceByRankForUnknownMarketCapRank() {

        webTestClient.get()
                .uri(uriBuilder -> createGetUrl(uriBuilder, 3))
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("errorMessage", "Crypto rank is not found.");

    }

    @Test
    @DisplayName("Test to create a crypto price by rank, success.")
    void testCreateCryptoPrice() {

        CoinPriceDto coinPriceDto = CoinPriceDto.builder()
                .marketCap(33577638586L)
                .currentPrice(94.99)
                .marketCapRank(8)
                .image("https://assets.coingecko.com/coins/images/8284/thumb/luna1557227471663.png?1567147072")
                .symbol("LUNA")
                .build();
        webTestClient.post()
                .uri(this::createListUrl)
                .bodyValue(coinPriceDto)
                .exchange()
                .expectStatus().isOk();

        CoinPrice cp = coinPriceRepository.findById(8).get();
        Assertions.assertThat(cp.getCurrentPrice()).isEqualTo(coinPriceDto.getCurrentPrice());
        Assertions.assertThat(cp.getImage()).isEqualTo(coinPriceDto.getImage());
        Assertions.assertThat(cp.getSymbol()).isEqualTo(coinPriceDto.getSymbol());
        Assertions.assertThat(cp.getMarketCap()).isEqualTo(coinPriceDto.getMarketCap());
        Assertions.assertThat(cp.getMarketCapRank()).isEqualTo(coinPriceDto.getMarketCapRank());

    }


    @Test
    @DisplayName("Test to update a crypto price by rank, success.")
    void testUpdateCryptoPrice() {

        CoinPriceDto coinPriceDto = CoinPriceDto.builder()
                .marketCap(373366510849L)
                .currentPrice(3101.01)
                .marketCapRank(2)
                .image("https://assets.coingecko.com/coins/images/279/large/ethereum.png?1595348880")
                .symbol("ETH")
                .build();
        webTestClient.put()
                .uri(this::createListUrl)
                .bodyValue(coinPriceDto)
                .exchange()
                .expectStatus().isOk();

        CoinPrice cp = coinPriceRepository.findById(2).get();
        Assertions.assertThat(cp.getCurrentPrice()).isEqualTo(coinPriceDto.getCurrentPrice());
        Assertions.assertThat(cp.getImage()).isEqualTo(coinPriceDto.getImage());
        Assertions.assertThat(cp.getSymbol()).isEqualTo(coinPriceDto.getSymbol());
        Assertions.assertThat(cp.getMarketCap()).isEqualTo(coinPriceDto.getMarketCap());
        Assertions.assertThat(cp.getMarketCapRank()).isEqualTo(coinPriceDto.getMarketCapRank());

    }

    @Test
    @DisplayName("Test to update a crypto price by rank for unknown rank, error.")
    void testUpdateCryptoPriceForUnknownRank() {

        CoinPriceDto coinPriceDto = CoinPriceDto.builder()
                .marketCap(373366510849L)
                .currentPrice(3101.01)
                .marketCapRank(4)
                .image("https://assets.coingecko.com/coins/images/279/large/ethereum.png?1595348880")
                .symbol("ETH")
                .build();
        webTestClient.put()
                .uri(this::createListUrl)
                .bodyValue(coinPriceDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("errorMessage", "Crypto rank is not found.");

    }

    @Test
    @DisplayName("Test to delete a crypto price by rank, success.")
    void testDeleteCryptoPriceByRank() {

        webTestClient.delete()
                .uri(uriBuilder -> createDeleteUrl(uriBuilder, 2))
                .exchange()
                .expectStatus().isOk();

    }

    @Test
    @DisplayName("Test to delete a crypto price by rank, error.")
    void testDeleteCryptoPriceByRankForUnknownMarketCapRank() {

        webTestClient.delete()
                .uri(uriBuilder -> createDeleteUrl(uriBuilder, 3))
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("errorMessage", "Crypto rank is not found.");

    }

}
