package io.github.solvo.application.ports.in;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferCommand(
        UUID senderWalletId,
        UUID receiverWalletId,
        BigDecimal transferValue
) {
}
