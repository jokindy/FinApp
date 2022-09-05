package com.jokindy.finapp.operation;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class OperationDto {

    private long id;

    @NotBlank(message = "Описание не может быть пустым")
    private String description;

    @Positive(message = "Сумма не может быть отрицательной")
    @NotNull(message = "Сумма не может быть пустой")
    private Double value;

    private long accountId;

    private OperationType type;
}
