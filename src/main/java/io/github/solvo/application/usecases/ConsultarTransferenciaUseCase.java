package io.github.solvo.application.usecases;

import io.github.solvo.application.ports.in.ConsultarTransferenciaUseCasePort;
import io.github.solvo.application.ports.out.TransferRepositoryPort;
import io.github.solvo.domain.entities.Transfer;
import io.github.solvo.domain.exceptions.TransferNotFoundException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ConsultarTransferenciaUseCase implements ConsultarTransferenciaUseCasePort {

    private final TransferRepositoryPort transferRepositoryPort;

    public ConsultarTransferenciaUseCase(TransferRepositoryPort transferRepositoryPort) {
        this.transferRepositoryPort = transferRepositoryPort;
    }

    @Override
    public Transfer consultar(UUID transferId) {
        return transferRepositoryPort.findTransferId(transferId)
                .orElseThrow(() -> new TransferNotFoundException(transferId));
    }
}
