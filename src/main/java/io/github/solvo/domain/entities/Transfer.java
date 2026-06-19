package io.github.solvo.domain.entities;

import io.github.solvo.domain.enums.TransferStatus;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class Transfer {
    private final UUID transferId;
    private final UUID senderId;
    private final UUID receiverId;
    private final BigDecimal value;
    private TransferStatus status;

    public Transfer(UUID transferId, UUID senderId, UUID receiverId, BigDecimal value) {
        this.transferId = transferId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.value = value;
        this.status = TransferStatus.PENDING;
    }

    public void approve() {
        if (this.status != TransferStatus.PENDING) {
            throw new IllegalStateException("Apenas transferências pendentes podem ser aprovadas");
        }
        this.status = TransferStatus.APPROVED;
    }

    public void recuse() {
        if (this.status != TransferStatus.PENDING) {
            throw new IllegalStateException("Apenas transferências pendentes podem ser recusadas");
        }
        this.status = TransferStatus.RECUSED;
    }
}
