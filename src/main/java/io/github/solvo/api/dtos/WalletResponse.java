package io.github.solvo.api.dtos;

import io.github.solvo.domain.enums.UserType;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletResponse(
        UUID id,
        UUID userId,
        UserType userType,
        BigDecimal balance
) {
}
