package io.github.solvo.api.mappers;

import io.github.solvo.api.dtos.CreateUserRequest;
import io.github.solvo.api.dtos.UserResponse;
import io.github.solvo.domain.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserApiMapper {
    UserResponse toResponse(User user);

    User toDomain(CreateUserRequest createUserRequest);
}
