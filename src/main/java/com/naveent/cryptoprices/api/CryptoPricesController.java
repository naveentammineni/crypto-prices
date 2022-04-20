package com.naveent.cryptoprices.api;

import com.naveent.cryptoprices.dto.CoinPriceDto;
import com.naveent.cryptoprices.model.CoinPrice;
import com.naveent.cryptoprices.service.CryptoPricesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.util.List;

@RestController
@RequestMapping("/prices")
public class CryptoPricesController {

    @Autowired
    private CryptoPricesService cryptoPricesService;

    @Operation(summary = "List all available Crypto prices.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class))})
    })
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CoinPriceDto> listAllCryptoPrices() {
        return cryptoPricesService.list().stream().map(this::convertToDto).toList();
    }

    @Operation(summary = "Read Crypto price of a particular rank.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CoinPriceDto.class))})
    })
    @GetMapping(value = "/{rank}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CoinPriceDto getCryptoPriceByRank(@PathVariable("rank") int marketCapRank) {
        CoinPrice coinPrice = cryptoPricesService.read(marketCapRank);
        return convertToDto(coinPrice);
    }

    @Operation(summary = "Create a new crypto price.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created.")
    })
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createNewCryptoPrice(@RequestBody CoinPriceDto coinPriceDto) {
        CoinPrice coinPrice = convertToModel(coinPriceDto);
        cryptoPricesService.create(coinPrice);
    }

    @Operation(summary = "update an existing crypto price.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated.")
    })
    @PutMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateCryptoPrice(@RequestBody CoinPriceDto coinPriceDto) {
        CoinPrice coinPrice = convertToModel(coinPriceDto);
        cryptoPricesService.update(coinPrice);
    }

    @Operation(summary = "Delete an existing Crypto price by the rank.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted.")
    })
    @DeleteMapping("/{rank}")
    public void deleteCryptoPrice(@PathVariable("rank") int marketCapRank) {
        cryptoPricesService.delete(marketCapRank);
    }

    private CoinPriceDto convertToDto(CoinPrice coinPrice) {
        return CoinPriceDto.builder()
                .marketCapRank(coinPrice.getMarketCapRank())
                .image(coinPrice.getImage())
                .symbol(coinPrice.getSymbol())
                .marketCap(coinPrice.getMarketCap())
                .currentPrice(coinPrice.getCurrentPrice())
                .build();
    }

    private CoinPrice convertToModel(CoinPriceDto coinPrice) {
        return CoinPrice.builder()
                .marketCapRank(coinPrice.getMarketCapRank())
                .image(coinPrice.getImage())
                .symbol(coinPrice.getSymbol())
                .marketCap(coinPrice.getMarketCap())
                .currentPrice(coinPrice.getCurrentPrice())
                .build();
    }

}
