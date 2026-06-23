package io.github.solvo.infrastructure.persistence.entities;

import io.github.solvo.domain.enums.TransferStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transfers")
@Getter
public class TransferJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "sender_wallet_id")
    private WalletJpaEntity senderWallet;

    @ManyToOne
    @JoinColumn(name = "receiver_wallet_id")
    private WalletJpaEntity receiverWallet;

    @Enumerated(EnumType.STRING)
    private TransferStatus status;

    private BigDecimal amount;
    private LocalDateTime createdAt;

    public TransferJpaEntity(UUID id, WalletJpaEntity senderWallet, WalletJpaEntity receiverWallet, BigDecimal amount, TransferStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.senderWallet = senderWallet;
        this.receiverWallet = receiverWallet;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public TransferJpaEntity(WalletJpaEntity senderWallet, WalletJpaEntity receiverWallet, BigDecimal amount, TransferStatus status) {
        this.senderWallet = senderWallet;
        this.receiverWallet = receiverWallet;
        this.amount = amount;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    protected TransferJpaEntity() {
    }
}
