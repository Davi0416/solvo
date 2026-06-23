package io.github.solvo.infrastructure.persistence.repositories;

import io.github.solvo.infrastructure.persistence.entities.WalletJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WalletJpaRepository extends JpaRepository<WalletJpaEntity, UUID> {
}
