package io.github.solvo.application.ports.in;

import io.github.solvo.domain.entities.Transfer;

import java.util.UUID;

public interface ConsultarTransferenciaUseCasePort {
    Transfer consultar(UUID transferId);
}
