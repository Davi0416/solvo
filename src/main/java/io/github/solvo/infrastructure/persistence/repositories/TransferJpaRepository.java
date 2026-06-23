package io.github.solvo.infrastructure.persistence.repositories;

import io.github.solvo.infrastructure.persistence.entities.TransferJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransferJpaRepository extends JpaRepository<TransferJpaEntity, UUID> {
}
