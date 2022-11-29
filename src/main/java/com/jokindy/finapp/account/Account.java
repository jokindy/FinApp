package com.jokindy.finapp.account;

import com.jokindy.finapp.currency.Currency;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private Double balance;

    @Column(name = "user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    private Currency currency;
}