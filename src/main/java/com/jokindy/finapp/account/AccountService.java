package com.jokindy.finapp.account;

import com.jokindy.finapp.account.dto.AccountDto;
import com.jokindy.finapp.account.dto.AccountShort;
import com.jokindy.finapp.currency.BankCurrencyService;
import com.jokindy.finapp.currency.Currency;
import com.jokindy.finapp.exception.BalanceIsNegativeException;
import com.jokindy.finapp.exception.NotEnoughMoneyException;
import com.jokindy.finapp.operation.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final BankCurrencyService bankCurrencyService;
    private final ModelMapper mapper;

    public void save(AccountDto accountDto, long userId) {
        Account account = mapper.map(accountDto, Account.class);
        account.setUserId(userId);
        accountRepository.save(account);
    }

    public Double getBalance(long userId) {
        List<AccountShort> accounts = accountRepository.getTotalBalanceByUserId(userId);
        double total = 0.0;
        for (AccountShort acc : accounts) {
            if (!acc.getCurrency().equals(Currency.USD)) {
                total = total + exchangeMoney(Currency.USD, acc.getCurrency(), acc.getBalance());
            } else {
                total = total + acc.getBalance();
            }
        }
        return total;
    }

    public void transfer(AccountTransferInfo accountTransferInfo) {
        AccountShort accountFrom = accountRepository.findBalanceById(accountTransferInfo.getAccountIdFrom());
        AccountShort accountTo = accountRepository.findBalanceById(accountTransferInfo.getAccountIdTo());
        double balanceFrom = accountFrom.getBalance();
        double balanceTo = accountTo.getBalance();
        double transfer = accountTransferInfo.getTransfer();
        if (balanceFrom < transfer) {
            throw new NotEnoughMoneyException();
        }
        if (!accountFrom.getCurrency().equals(accountTo.getCurrency())) {
            double exchangedTransfer = exchangeMoney(accountTo.getCurrency(), accountFrom.getCurrency(), transfer);
            balanceFrom = balanceFrom - transfer;
            balanceTo = balanceTo + exchangedTransfer;
        } else {
            balanceFrom = balanceFrom - transfer;
            balanceTo = balanceTo + transfer;
        }
        accountRepository.updateBalanceById(balanceFrom, accountTransferInfo.getAccountIdFrom());
        accountRepository.updateBalanceById(balanceTo, accountTransferInfo.getAccountIdTo());
    }

    public List<Account> getAccounts(long userId) {
        return accountRepository.findAllByUserIdOrderByIdAsc(userId);
    }

    public void handleOperation(Operation operation) {
        long accountId = operation.getAccountId();
        Optional<Account> accountO = accountRepository.findById(accountId);
        double balance = accountO.get().getBalance();
        Currency accountCurrency = accountO.get().getCurrency();
        Currency operationCurrency = operation.getCurrency();
        double operationValue = operation.getValue();
        if (!accountCurrency.equals(operationCurrency)) {
            operationValue = exchangeMoney(accountCurrency, operationCurrency, operationValue);
        }
        balance = balance + operationValue;
        if (balance < 0) {
            throw new BalanceIsNegativeException();
        }
        accountRepository.updateBalanceById(balance, accountId);
    }

    private double exchangeMoney(Currency original, Currency target, double value) {
        double rate = bankCurrencyService.getRate(original, target);
        return value / rate;
    }
}