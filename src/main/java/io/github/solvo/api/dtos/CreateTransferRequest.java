package io.github.solvo.api.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateTransferRequest(
        @NotNull UUID senderWalletId,
        @NotNull UUID receiverWalletId,
        @NotNull @Positive BigDecimal transferValue
) {
}

