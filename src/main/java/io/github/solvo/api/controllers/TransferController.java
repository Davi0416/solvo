package io.github.solvo.api.controllers;

import io.github.solvo.api.dtos.CreateTransferRequest;
import io.github.solvo.api.dtos.TransferResponse;
import io.github.solvo.api.mappers.TransferApiMapper;
import io.github.solvo.application.ports.in.ConsultarTransferenciaUseCasePort;
import io.github.solvo.application.ports.in.TransferUseCasePort;
import io.github.solvo.infrastructure.cache.TransferIdempotencyStore;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transfers")
public class TransferController {

    private final TransferApiMapper transferApiMapper;
    private final TransferUseCasePort transferUseCasePort;
    private final ConsultarTransferenciaUseCasePort consultarTransferenciaUseCasePort;
    private final TransferIdempotencyStore idempotencyStore;

    public TransferController(TransferApiMapper transferApiMapper,
                              TransferUseCasePort transferUseCasePort,
                              ConsultarTransferenciaUseCasePort consultarTransferenciaUseCasePort,
                              TransferIdempotencyStore idempotencyStore) {
        this.transferApiMapper = transferApiMapper;
        this.transferUseCasePort = transferUseCasePort;
        this.consultarTransferenciaUseCasePort = consultarTransferenciaUseCasePort;
        this.idempotencyStore = idempotencyStore;
    }

    @Cacheable(cacheNames = "transfers", key = "#id")
    @GetMapping("/{id}")
    public TransferResponse getTransfer(@PathVariable UUID id) {
        return transferApiMapper.toResponse(consultarTransferenciaUseCasePort.consultar(id));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TransferResponse createTransfer(@RequestBody CreateTransferRequest request,
                                            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {
        if (idempotencyKey != null) {
            var cached = idempotencyStore.find(idempotencyKey);
            if (cached.isPresent()) {
                return cached.get();
            }
        }

        var transfer = transferUseCasePort.transfer(transferApiMapper.toCommand(request));
        var response = transferApiMapper.toResponse(transfer);

        if (idempotencyKey != null) {
            idempotencyStore.save(idempotencyKey, response);
        }

        return response;
    }
}

