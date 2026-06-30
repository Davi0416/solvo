package io.github.solvo.infrastructure.persistence.repositories;

import io.github.solvo.infrastructure.persistence.entities.WalletJpaEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface WalletJpaRepository extends JpaRepository<WalletJpaEntity, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM WalletJpaEntity w WHERE w.id = :id")
    Optional<WalletJpaEntity> findByIdWithLock(@Param("id") UUID id);
}
