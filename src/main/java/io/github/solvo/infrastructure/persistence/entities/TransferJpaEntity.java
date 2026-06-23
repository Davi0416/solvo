package io.github.solvo.infrastructure.persistence.entities;

import io.github.solvo.domain.enums.TransferStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "transfers")
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

    private BigDecimal amount;
    private TransferStatus status;

    public TransferJpaEntity(UUID id, WalletJpaEntity senderWallet, WalletJpaEntity receiverWallet, BigDecimal amount, TransferStatus status) {
        this.id = id;
        this.senderWallet = senderWallet;
        this.receiverWallet = receiverWallet;
        this.amount = amount;
        this.status = status;
    }

    public TransferJpaEntity(WalletJpaEntity senderWallet, WalletJpaEntity receiverWallet, BigDecimal amount, TransferStatus status) {
        this.senderWallet = senderWallet;
        this.receiverWallet = receiverWallet;
        this.amount = amount;
        this.status = status;
    }

    protected TransferJpaEntity() {
    }
}
