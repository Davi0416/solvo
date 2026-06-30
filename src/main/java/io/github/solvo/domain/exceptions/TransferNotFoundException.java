package io.github.solvo.domain.exceptions;

import java.util.UUID;

public class TransferNotFoundException extends RuntimeException {
    public TransferNotFoundException(UUID transferId) {
        super("Transferência não encontrada");
    }
}
