package io.github.solvo.infrastructure.persistence.adapters;

import io.github.solvo.application.ports.out.WalletRepositoryPort;
import io.github.solvo.domain.entities.Wallet;
import io.github.solvo.infrastructure.persistence.mappers.WalletMapper;
import io.github.solvo.infrastructure.persistence.repositories.WalletJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class WalletRepositoryAdapter implements WalletRepositoryPort {
    private final WalletJpaRepository walletJpaRepository;
    private final WalletMapper walletMapper;

    public WalletRepositoryAdapter(WalletJpaRepository walletJpaRepository, WalletMapper walletMapper) {
        this.walletJpaRepository = walletJpaRepository;
        this.walletMapper = walletMapper;
    }

    @Override
    public Optional<Wallet> findById(UUID walletId) {
        return walletJpaRepository.findById(walletId)
                .map(walletMapper::toDomain);
    }

    @Override
    public Wallet save(Wallet wallet) {
        return walletMapper.toDomain(walletJpaRepository.save(walletMapper.toJpa(wallet)));
    }
}
