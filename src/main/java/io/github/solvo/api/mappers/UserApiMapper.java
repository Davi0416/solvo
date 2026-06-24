package io.github.solvo.api.mappers;

import io.github.solvo.api.dtos.CreateUserRequest;
import io.github.solvo.api.dtos.UserResponse;
import io.github.solvo.domain.entities.User;


public interface UserApiMapper {
    UserResponse toResponse(User user);

    User toCommand(CreateUserRequest createUserRequest);
}
