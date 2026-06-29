package io.github.solvo.infrastructure.persistence.mappers;

import io.github.solvo.domain.entities.Wallet;
import io.github.solvo.domain.exceptions.UserNotFoundException;
import io.github.solvo.infrastructure.persistence.entities.WalletJpaEntity;
import io.github.solvo.infrastructure.persistence.repositories.UserJpaRepository;
import io.github.solvo.infrastructure.persistence.repositories.WalletJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class WalletMapperImpl implements WalletMapper {

    private final UserJpaRepository userJpaRepository;
    private final WalletJpaRepository walletJpaRepository;

    public WalletMapperImpl(UserJpaRepository userJpaRepository, WalletJpaRepository walletJpaRepository) {
        this.userJpaRepository = userJpaRepository;
        this.walletJpaRepository = walletJpaRepository;
    }

    @Override
    public WalletJpaEntity toJpa(Wallet wallet) {
        return walletJpaRepository.findById(wallet.getId())
                .map(existing -> {
                    existing.updateBalance(wallet.getBalance());
                    return existing;
                })
                .orElseGet(() -> new WalletJpaEntity(
                        userJpaRepository.findById(wallet.getUserId())
                                .orElseThrow(() -> new UserNotFoundException(wallet.getUserId())),
                        wallet.getUserType(),
                        wallet.getBalance()
                ));
    }

    @Override
    public Wallet toDomain(WalletJpaEntity walletJpaEntity) {
        return new Wallet(
                walletJpaEntity.getId(),
                walletJpaEntity.getUser().getId(),
                walletJpaEntity.getUserType(),
                walletJpaEntity.getBalance()
        );
    }
}
