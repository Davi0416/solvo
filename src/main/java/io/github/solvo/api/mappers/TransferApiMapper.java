package io.github.solvo.api.mappers;

import io.github.solvo.api.dtos.CreateTransferRequest;
import io.github.solvo.api.dtos.TransferResponse;
import io.github.solvo.application.ports.in.TransferCommand;
import io.github.solvo.domain.entities.Transfer;


public interface TransferApiMapper {
    TransferResponse toResponse(Transfer transfer);

    TransferCommand toCommand(CreateTransferRequest createTransferRequest);
}

