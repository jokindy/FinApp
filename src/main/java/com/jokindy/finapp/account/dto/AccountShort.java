package com.jokindy.finapp.account.dto;

import com.jokindy.finapp.currency.Currency;

public interface AccountShort {

    Currency getCurrency();

    double getBalance();
}
