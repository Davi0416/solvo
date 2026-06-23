package io.github.solvo.application.ports.out;

import io.github.solvo.domain.entities.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {
    Optional<User> findById(UUID userId);

    User save(User user);
}
