package io.github.solvo.domain.exceptions;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException() {
        super("Acesso negado");
    }
}
