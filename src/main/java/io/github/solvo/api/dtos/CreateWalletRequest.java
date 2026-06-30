package io.github.solvo.api.dtos;

import io.github.solvo.domain.enums.UserType;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateWalletRequest(
        UUID userId,
        @NotNull UserType userType
) {
}
