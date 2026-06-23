package io.github.solvo.infrastructure.authorization;

public record AuthorizationResponse(String status, AuthorizationData data) {
    public record AuthorizationData(boolean authorization) {
    }
}
