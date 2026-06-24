package io.github.solvo.infrastructure.persistence.mappers;

import io.github.solvo.domain.entities.User;
import io.github.solvo.infrastructure.persistence.entities.UserJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserJpaEntity toJpa(User user) {
        return new UserJpaEntity(
                user.getName(),
                user.getDocument(),
                user.getEmail(),
                user.getUserType(),
                user.getPhone()
        );
    }

    @Override
    public User toDomain(UserJpaEntity entity) {
        return new User(
                entity.getId(),
                entity.getName(),
                entity.getDocument(),
                entity.getUserType(),
                entity.getEmail(),
                entity.getPhone()
        );
    }
}
