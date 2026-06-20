package io.github.solvo.application.ports.in;

import io.github.solvo.domain.entities.Transfer;

public interface TransferUseCasePort {
    Transfer transfer(TransferCommand transferInfo);
}
