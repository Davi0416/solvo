package io.github.solvo.api.dtos;

public record LoginRequest(
        String email,
        String password
) {
}
