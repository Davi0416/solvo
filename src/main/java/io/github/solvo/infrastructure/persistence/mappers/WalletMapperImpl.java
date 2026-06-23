package io.github.solvo.infrastructure.persistence.mappers;

import io.github.solvo.domain.entities.Wallet;
import io.github.solvo.domain.exceptions.UserNotFoundException;
import io.github.solvo.infrastructure.persistence.entities.WalletJpaEntity;
import io.github.solvo.infrastructure.persistence.repositories.UserJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class WalletMapperImpl implements WalletMapper {

    private final UserJpaRepository userJpaRepository;

    public WalletMapperImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public WalletJpaEntity toJpa(Wallet wallet) {
        return new WalletJpaEntity(
                userJpaRepository.findById(wallet.getUserId())
                        .orElseThrow(() -> new UserNotFoundException(wallet.getUserId())),
                wallet.getUserType(),
                wallet.getBalance()
        );

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
