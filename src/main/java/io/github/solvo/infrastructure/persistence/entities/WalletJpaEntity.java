package io.github.solvo.infrastructure.persistence.entities;

import io.github.solvo.domain.enums.UserType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "wallets")
@Getter
public class WalletJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserJpaEntity user;

    private UserType userType;
    private BigDecimal balance;

    public WalletJpaEntity(UUID id, UserJpaEntity user, UserType userType, BigDecimal balance) {
        this.id = id;
        this.user = user;
        this.userType = userType;
        this.balance = balance;
    }

    public WalletJpaEntity(UserJpaEntity user, UserType userType, BigDecimal balance) {
        this.user = user;
        this.userType = userType;
        this.balance = balance;
    }

    protected WalletJpaEntity() {
    }
}
