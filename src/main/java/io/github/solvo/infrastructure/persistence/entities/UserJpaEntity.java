package io.github.solvo.infrastructure.persistence.entities;

import io.github.solvo.domain.enums.UserType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
public class UserJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    private String name;
    private String document;
    private String email;
    private String password;
    private String phone;
    private LocalDateTime createdAt;

    public UserJpaEntity(UUID id, String name, String document, String email, UserType userType, String phone, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.document = document;
        this.userType = userType;
        this.email = email;
        this.phone = phone;
        this.createdAt = createdAt;
    }

    public UserJpaEntity(String name, String document, String email, UserType userType, String password, String phone) {
        this.name = name;
        this.document = document;
        this.userType = userType;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.createdAt = LocalDateTime.now();
    }

    protected UserJpaEntity() {
    }
}
