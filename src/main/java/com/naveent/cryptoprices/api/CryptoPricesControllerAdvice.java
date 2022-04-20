package com.naveent.cryptoprices.api;

import com.naveent.cryptoprices.dto.AlternateResponse;
import com.naveent.cryptoprices.exceptions.CoinRankNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class CryptoPricesControllerAdvice {

    @ExceptionHandler(CoinRankNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<AlternateResponse> coinRankNotFoundException(CoinRankNotFoundException ex) {
        log.error("log_type=CryptoPricesService, errorCode={}", HttpStatus.NOT_FOUND);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(new AlternateResponse("Crypto rank is not found."), headers, HttpStatus.NOT_FOUND);
    }
}
