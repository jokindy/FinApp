package com.jokindy.finapp.account;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
public class AccountDto {

    @NotBlank(message = "Описание не может быть пустым")
    private String description;

    @Positive(message = "Сумма не может быть отрицательной")
    private Double balance;
}
