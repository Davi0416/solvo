package io.github.solvo.domain.entities;

import io.github.solvo.domain.enums.UserType;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class Wallet {
    private final UUID id;
    private final UUID userId;
    private final UserType userType;
    private BigDecimal balance;

    public Wallet(UUID id, UUID userId, UserType userType, BigDecimal balance) {
        this.id = id;
        this.userId = userId;
        this.userType = userType;
        this.balance = balance;
    }

    public Transfer transfer(BigDecimal transferValue, UUID receiverWalletId) {
        if (this.userType == UserType.MERCHANT)
            throw new IllegalArgumentException("Lojistas não têm permissão para realizar transferências.");
        if (transferValue.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Saldo zerado não pode transferir!");
        if (transferValue.compareTo(this.balance) > 0)
            throw new IllegalArgumentException("Saldo insuficiente para concluir a transferência");

        this.balance = this.balance.subtract(transferValue);
        return new Transfer(this.id, receiverWalletId, transferValue);
    }

    public void refund(BigDecimal transferValue) {
        if (transferValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor de estorno inválido");
        }
        this.balance = this.balance.add(transferValue);
    }

    public void receive(BigDecimal transferValue) {
        this.balance = this.balance.add(transferValue);
    }
}
