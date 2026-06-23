package io.github.solvo.domain.entities;

import io.github.solvo.domain.enums.TransferStatus;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class Transfer {
    private final UUID transferId;
    private final UUID senderWalletId;
    private final UUID receiverWalletId;
    private final BigDecimal amount;
    private TransferStatus status;

    public Transfer(UUID transferId, UUID senderWalletId, UUID receiverWalletId, BigDecimal amount) {
        this.transferId = transferId;
        this.senderWalletId = senderWalletId;
        this.receiverWalletId = receiverWalletId;
        this.amount = amount;
        this.status = TransferStatus.PENDING;
    }

    public Transfer(UUID senderWalletId, UUID receiverWalletId, BigDecimal amount) {
        this.transferId = UUID.randomUUID();
        this.senderWalletId = senderWalletId;
        this.receiverWalletId = receiverWalletId;
        this.amount = amount;
        this.status = TransferStatus.PENDING;
    }

    private void approve() {
        if (this.status != TransferStatus.PENDING) {
            throw new IllegalStateException("Apenas transferências pendentes podem ser aprovadas");
        }
        this.status = TransferStatus.APPROVED;
    }

    private void recuse() {
        if (this.status != TransferStatus.PENDING) {
            throw new IllegalStateException("Apenas transferências pendentes podem ser recusadas");
        }
        this.status = TransferStatus.REJECTED;
    }

    public void processAuthorization(boolean isAuthorized) {
        if (isAuthorized) {
            this.approve();
        } else {
            this.recuse();
        }
    }

}
