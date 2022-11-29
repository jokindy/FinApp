package com.jokindy.finapp.operation.dto;

import com.jokindy.finapp.currency.Currency;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class OperationShortDto {

    private String description;
    private Double value;
    private LocalDateTime created;
    private Currency currency;
    private AccountShortDto account;

    @Data
    static class AccountShortDto {
        private String description;
    }

    public String getCreated() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return formatter.format(created);
    }
}
