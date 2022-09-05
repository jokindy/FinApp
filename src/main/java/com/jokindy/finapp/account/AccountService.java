package com.jokindy.finapp.account;

import com.jokindy.finapp.exception.NotEnoughMoneyException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final ModelMapper mapper;

    public void save(AccountDto accountDto, long userId) {
        Account account = mapper.map(accountDto, Account.class);
        account.setUserId(userId);
        accountRepository.save(account);
    }

    public Double getBalance(long userId) {
        Double balance = accountRepository.getTotalBalanceByUserId(userId);
        return Objects.requireNonNullElse(balance, 0.0);
    }

    public void transfer(AccountTransferInfo accountTransferInfo) {
        double balanceFrom = accountRepository.findBalanceById(accountTransferInfo.getAccountIdFrom());
        double balanceTo = accountRepository.findBalanceById(accountTransferInfo.getAccountIdTo());
        double transfer = accountTransferInfo.getTransfer();
        if (balanceFrom < transfer) {
            throw new NotEnoughMoneyException();
        }
        balanceFrom = balanceFrom - transfer;
        balanceTo = balanceTo + transfer;
        accountRepository.updateBalanceById(balanceFrom, accountTransferInfo.getAccountIdFrom());
        accountRepository.updateBalanceById(balanceTo, accountTransferInfo.getAccountIdTo());
    }

    public List<Account> getAccounts(long userId) {
        return accountRepository.findAllByUserIdOrderByIdAsc(userId);
    }
}
