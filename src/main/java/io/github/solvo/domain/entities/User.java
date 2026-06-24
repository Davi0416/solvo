package io.github.solvo.domain.entities;

import io.github.solvo.domain.enums.UserType;
import lombok.Getter;

import java.util.UUID;

@Getter
public class User {
    private final UUID id;
    private final String name;
    private final String document;
    private final UserType userType;
    private String email;
    private String phone;

    public User(UUID id, String name, String document, UserType userType, String email, String phone) {
        this.id = id;
        this.name = name;
        this.document = document;
        this.userType = userType;
        this.email = email;
        this.phone = phone;
    }

    public User(String name, String document, UserType userType, String email, String phone) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.document = document;
        this.userType = userType;
        this.email = email;
        this.phone = phone;
    }
}
