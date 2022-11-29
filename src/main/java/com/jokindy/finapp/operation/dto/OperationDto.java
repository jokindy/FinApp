package com.jokindy.finapp.operation.dto;

import com.jokindy.finapp.currency.Currency;
import com.jokindy.finapp.operation.OperationType;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
public class OperationDto {

    private long id;

    @NotBlank(message = "Описание не может быть пустым")
    private String description;

    @Positive(message = "Сумма не может быть отрицательной")
    @NotNull(message = "Сумма не может быть пустой")
    private Double value;

    private long accountId;

    private LocalDateTime created = LocalDateTime.now();

    private OperationType type;

    private Currency currency;
}
