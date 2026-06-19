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

    public User(UUID id, String name, String document, String email, UserType userType, String phone) {
        this.id = id;
        this.name = name;
        this.document = document;
        this.userType = userType;
        this.email = email;
        this.phone = phone;
    }
}
