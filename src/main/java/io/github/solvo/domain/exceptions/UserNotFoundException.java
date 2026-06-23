package io.github.solvo.domain.exceptions;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(UUID userId) {
        super(String.format("Usuário com o ID %s não encontrado.", userId));
    }
}
