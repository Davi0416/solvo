package io.github.solvo.infrastructure.persistence.adapters;

import io.github.solvo.application.ports.out.TransferRepositoryPort;
import io.github.solvo.domain.entities.Transfer;
import io.github.solvo.infrastructure.persistence.mappers.TransferMapper;
import io.github.solvo.infrastructure.persistence.repositories.TransferJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;


@Component
public class TransferRepositoryAdapter implements TransferRepositoryPort {

    private final TransferJpaRepository transferJpaRepository;
    private final TransferMapper transferMapper;

    public TransferRepositoryAdapter(TransferJpaRepository transferJpaRepository, TransferMapper transferMapper) {
        this.transferJpaRepository = transferJpaRepository;
        this.transferMapper = transferMapper;
    }

    @Override
    public Optional<Transfer> findTransferId(UUID transferId) {
        return transferJpaRepository.findById(transferId)
                .map(transferMapper::toDomain);
    }

    @Override
    public Transfer save(Transfer transfer) {
        return transferMapper.toDomain(transferJpaRepository.save(transferMapper.toJpa(transfer)));
    }
}
