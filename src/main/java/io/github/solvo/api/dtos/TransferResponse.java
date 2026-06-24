package io.github.solvo.api.dtos;

import io.github.solvo.domain.enums.TransferStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferResponse(
        UUID transferId,
        UUID senderWalletId,
        UUID receiverWalletId,
        BigDecimal amount,
        TransferStatus status
) {
}

