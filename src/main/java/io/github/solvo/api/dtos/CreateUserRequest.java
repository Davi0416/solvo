package io.github.solvo.api.dtos;

import io.github.solvo.domain.enums.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(
        @NotBlank String name,
        @NotBlank String document,
        @NotNull UserType userType,
        @NotBlank @Email String email,
        @NotBlank String password,
        @NotBlank String phone
) {
}
