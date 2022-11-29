package com.jokindy.finapp.account.dto;


import com.jokindy.finapp.currency.Currency;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
public class AccountDto {

    @NotBlank(message = "Описание не может быть пустым")
    @Size(min = 10, max = 100)
    private String description;

    @Positive(message = "Сумма не может быть отрицательной")
    private Double balance;

    private Currency currency;
}
