package io.github.solvo.api.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateTransferRequest(
        UUID senderWalletId,
        UUID receiverWalletId,
        BigDecimal transferValue
) {
}

