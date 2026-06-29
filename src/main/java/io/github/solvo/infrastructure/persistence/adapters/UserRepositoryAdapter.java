package io.github.solvo.infrastructure.persistence.adapters;

import io.github.solvo.application.ports.out.UserRepositoryPort;
import io.github.solvo.domain.entities.User;
import io.github.solvo.infrastructure.persistence.mappers.UserMapper;
import io.github.solvo.infrastructure.persistence.repositories.UserJpaRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;

    public UserRepositoryAdapter(UserJpaRepository userJpaRepository, UserMapper userMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Optional<User> findById(UUID userId) {
        return userJpaRepository.findById(userId)
                .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(userMapper::toDomain);
    }

    @Override
    @CacheEvict(cacheNames = "users", key = "#user.id")
    public User save(User user) {
        return userMapper.toDomain(userJpaRepository.save(userMapper.toJpa(user)));
    }
}
