package io.github.solvo.application.ports.out;

import io.github.solvo.domain.entities.Wallet;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepositoryPort {
    Optional<Wallet> findByWalletId(UUID walletId);

    Wallet save(Wallet wallet);
}
