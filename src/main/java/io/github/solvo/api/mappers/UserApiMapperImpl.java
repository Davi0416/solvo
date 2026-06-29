package io.github.solvo.api.mappers;

import io.github.solvo.api.dtos.CreateUserRequest;
import io.github.solvo.api.dtos.UserResponse;
import io.github.solvo.domain.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserApiMapperImpl implements UserApiMapper {

    @Override
    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getDocument(),
                user.getUserType(),
                user.getEmail(),
                user.getPhone()
        );
    }

    @Override
    public User toCommand(CreateUserRequest entity) {
        return new User(
                entity.name(),
                entity.document(),
                entity.userType(),
                entity.email(),
                entity.password(),
                entity.phone()
        );
    }
}
