package com.jokindy.finapp.account;

import com.jokindy.finapp.account.validation.IdMatches;
import lombok.Data;

import javax.validation.constraints.Positive;

@Data
@IdMatches()
public class AccountTransferInfo {

    private Long accountIdFrom;
    private Long accountIdTo;

    @Positive(message = "Сумма не может быть отрицательной")
    private Double transfer;
}
