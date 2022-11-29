package com.jokindy.finapp.currency;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;


@Service
public class BankCurrencyService {

    @SneakyThrows
    public Double getRate(Currency original, Currency target) {
        String url = String.format("https://api.apilayer.com/exchangerates_data/latest?symbols=%s&base=%s", target, original);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("apikey", "P3U8NsQPCAZkDwiy7cgwNZrF9leu5Qe8")
                .GET()
                .build();
        HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());
        Callback callback = new ObjectMapper().readValue(response.body(), Callback.class);
        return callback.getRates().get(target);
    }

    @Data
    static class Callback {
        private String base;
        private boolean success;
        private Map<Currency, Double> rates;
        private int timestamp;
        private String date;
    }
}