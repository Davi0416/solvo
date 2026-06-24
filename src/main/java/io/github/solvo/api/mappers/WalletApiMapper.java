package io.github.solvo.api.mappers;

import io.github.solvo.api.dtos.CreateWalletRequest;
import io.github.solvo.api.dtos.WalletResponse;
import io.github.solvo.domain.entities.Wallet;


public interface WalletApiMapper {
    WalletResponse toResponse(Wallet wallet);

    Wallet toCommand(CreateWalletRequest walletResponse);
}
