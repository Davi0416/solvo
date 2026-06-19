package io.github.solvo.domain.entities;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class Wallet {
    private final UUID id;
    private final UUID userId;
    private BigDecimal balance;

    public Wallet(UUID id, UUID userId, BigDecimal balance) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
    }
}
