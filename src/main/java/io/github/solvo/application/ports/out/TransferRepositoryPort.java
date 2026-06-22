package io.github.solvo.application.ports.out;

import io.github.solvo.domain.entities.Transfer;

import java.util.Optional;
import java.util.UUID;

public interface TransferRepositoryPort {
    Optional<Transfer> findTransferId(UUID transferId);

    Transfer save(Transfer transfer);
}
