package com.jokindy.finapp.account;

import com.jokindy.finapp.account.dto.AccountShort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findAllByUserIdOrderByIdAsc(long userId);

    @Query(value = "select currency, balance from accounts where id = ?1", nativeQuery = true)
    AccountShort findBalanceById(long id);

    @Query(value = "select currency, balance from accounts where user_id = ?1", nativeQuery = true)
    List<AccountShort> getTotalBalanceByUserId(long userId);

    @Modifying
    @Query(value = "update accounts set balance = ?1 where id = ?2", nativeQuery = true)
    void updateBalanceById(double balance, long userId);
}
