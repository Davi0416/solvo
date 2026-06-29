package io.github.solvo.infrastructure.persistence.mappers;

import io.github.solvo.domain.entities.Transfer;
import io.github.solvo.domain.exceptions.WalletNotFoundException;
import io.github.solvo.infrastructure.persistence.entities.TransferJpaEntity;
import io.github.solvo.infrastructure.persistence.repositories.WalletJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class TransferMapperImpl implements TransferMapper {

    private final WalletJpaRepository walletJpaRepository;

    public TransferMapperImpl(WalletJpaRepository walletJpaRepository) {
        this.walletJpaRepository = walletJpaRepository;
    }

    @Override
    public TransferJpaEntity toJpa(Transfer transfer) {
        return new TransferJpaEntity(
                walletJpaRepository.findById(transfer.getSenderWalletId())
                        .orElseThrow(() -> new WalletNotFoundException(transfer.getSenderWalletId())),
                walletJpaRepository.findById(transfer.getReceiverWalletId())
                        .orElseThrow(() -> new WalletNotFoundException(transfer.getReceiverWalletId())),
                transfer.getAmount(),
                transfer.getStatus()
        );
    }

    @Override
    public Transfer toDomain(TransferJpaEntity transferJpaEntity) {
        Transfer transfer = new Transfer(
                transferJpaEntity.getId(),
                transferJpaEntity.getSenderWallet().getId(),
                transferJpaEntity.getReceiverWallet().getId(),
                transferJpaEntity.getAmount()
        );

        if (transferJpaEntity.getStatus() != null && transferJpaEntity.getStatus() != transfer.getStatus()) {
            switch (transferJpaEntity.getStatus()) {
                case APPROVED -> transfer.processAuthorization(true);
                case REJECTED -> transfer.processAuthorization(false);
                default -> {
                }
            }
        }

        return transfer;
    }
}
