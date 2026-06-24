package io.github.solvo.api.dtos;

import io.github.solvo.domain.enums.UserType;

public record CreateUserRequest(
        String name,
        String document,
        UserType userType,
        String email,
        String phone
) {
}
