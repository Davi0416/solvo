package io.github.solvo.api.mappers;

import io.github.solvo.api.dtos.CreateTransferRequest;
import io.github.solvo.api.dtos.TransferResponse;
import io.github.solvo.application.ports.in.TransferCommand;
import io.github.solvo.domain.entities.Transfer;
import org.springframework.stereotype.Component;

@Component
public class TransferApiMapperImpl implements TransferApiMapper {

    @Override
    public TransferResponse toResponse(Transfer transfer) {
        return new TransferResponse(
                transfer.getTransferId(),
                transfer.getSenderWalletId(),
                transfer.getReceiverWalletId(),
                transfer.getAmount(),
                transfer.getStatus()
        );
    }

    @Override
    public TransferCommand toCommand(CreateTransferRequest request) {
        return new TransferCommand(
                request.senderWalletId(),
                request.receiverWalletId(),
                request.transferValue()
        );
    }
}
