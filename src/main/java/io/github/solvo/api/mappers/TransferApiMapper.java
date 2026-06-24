package io.github.solvo.api.mappers;

import io.github.solvo.api.dtos.CreateTransferRequest;
import io.github.solvo.api.dtos.TransferResponse;
import io.github.solvo.application.ports.in.TransferCommand;
import io.github.solvo.domain.entities.Transfer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransferApiMapper {
    TransferResponse toResponse(Transfer transfer);

    TransferCommand toCommand(CreateTransferRequest createTransferRequest);
}

