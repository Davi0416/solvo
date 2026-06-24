package io.github.solvo.api.mappers;

import io.github.solvo.api.dtos.CreateWalletRequest;
import io.github.solvo.api.dtos.WalletResponse;
import io.github.solvo.domain.entities.Wallet;
import org.springframework.stereotype.Component;

@Component
public class WalletApiMapperImpl implements WalletApiMapper {
    @Override
    public WalletResponse toResponse(Wallet wallet) {
        return new WalletResponse(
                wallet.getId(),
                wallet.getUserId(),
                wallet.getUserType(),
                wallet.getBalance()
        );
    }

    @Override
    public Wallet toCommand(CreateWalletRequest entity) {
        return new Wallet(
                entity.userId(),
                entity.userType()
        );
    }
}
