package io.github.solvo.api.mappers;

import io.github.solvo.api.dtos.CreateWalletRequest;
import io.github.solvo.api.dtos.WalletResponse;
import io.github.solvo.domain.entities.Wallet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletApiMapper {
    WalletResponse toResponse(Wallet wallet);

    Wallet toDomain(CreateWalletRequest walletResponse);
}
