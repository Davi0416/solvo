package io.github.solvo.api.dtos;

import io.github.solvo.domain.enums.UserType;

import java.util.UUID;

public record CreateWalletRequest(
        UUID userId,
        UserType userType
) {
}
