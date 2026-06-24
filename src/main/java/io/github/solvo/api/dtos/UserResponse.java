package io.github.solvo.api.dtos;

import io.github.solvo.domain.enums.UserType;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String document,
        UserType userType,
        String email,
        String phone
) {
}
