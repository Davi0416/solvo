package io.github.solvo.api.controllers;

import io.github.solvo.api.dtos.CreateTransferRequest;
import io.github.solvo.api.dtos.TransferResponse;
import io.github.solvo.api.mappers.TransferApiMapper;
import io.github.solvo.application.ports.in.TransferUseCasePort;
import io.github.solvo.application.ports.out.TransferRepositoryPort;
import io.github.solvo.domain.exceptions.TransferNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transfers")
public class TransferController {

    private final TransferApiMapper transferApiMapper;
    private final TransferUseCasePort transferUseCasePort;
    private final TransferRepositoryPort transferRepositoryPort;

    public TransferController(TransferApiMapper transferApiMapper,
                              TransferUseCasePort transferUseCasePort,
                              TransferRepositoryPort transferRepositoryPort) {
        this.transferApiMapper = transferApiMapper;
        this.transferUseCasePort = transferUseCasePort;
        this.transferRepositoryPort = transferRepositoryPort;
    }

    @GetMapping("/{id}")
    public TransferResponse getTransfer(@PathVariable UUID id) {
        return transferRepositoryPort.findTransferId(id)
                .map(transferApiMapper::toResponse)
                .orElseThrow(() -> new TransferNotFoundException(id));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TransferResponse createTransfer(@RequestBody CreateTransferRequest request) {
        var transfer = transferUseCasePort.transfer(transferApiMapper.toCommand(request));
        return transferApiMapper.toResponse(transfer);
    }
}

