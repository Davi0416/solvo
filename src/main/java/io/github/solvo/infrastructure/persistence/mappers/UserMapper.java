package io.github.solvo.infrastructure.persistence.mappers;

import io.github.solvo.domain.entities.User;
import io.github.solvo.infrastructure.persistence.entities.UserJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserJpaEntity toJpa(User user);

    User toDomain(UserJpaEntity userJpaEntity);
}
