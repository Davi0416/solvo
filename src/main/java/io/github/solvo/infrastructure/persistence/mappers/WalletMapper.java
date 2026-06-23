package io.github.solvo.infrastructure.persistence.mappers;

import io.github.solvo.domain.entities.Wallet;
import io.github.solvo.infrastructure.persistence.entities.WalletJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletMapper {
    WalletJpaEntity toJpa(Wallet wallet);

    Wallet toDomain(WalletJpaEntity walletJpaEntity);
}
