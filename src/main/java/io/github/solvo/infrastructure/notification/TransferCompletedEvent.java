package io.github.solvo.infrastructure.notification;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferCompletedEvent(
        UUID transferId,
        UUID senderWalletId,
        UUID receiverWalletId,
        BigDecimal amount
) {
}
