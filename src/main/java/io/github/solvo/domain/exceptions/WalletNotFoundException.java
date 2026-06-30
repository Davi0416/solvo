package io.github.solvo.domain.exceptions;

import java.util.UUID;

public class WalletNotFoundException extends RuntimeException {
    public WalletNotFoundException(UUID walletId) {
        super("Carteira não encontrada");
    }
}
