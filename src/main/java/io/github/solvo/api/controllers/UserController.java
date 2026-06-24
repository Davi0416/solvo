package io.github.solvo.api.controllers;

import io.github.solvo.api.dtos.CreateUserRequest;
import io.github.solvo.api.dtos.UserResponse;
import io.github.solvo.api.mappers.UserApiMapper;
import io.github.solvo.application.ports.out.UserRepositoryPort;
import io.github.solvo.domain.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserRepositoryPort userRepositoryPort;
    private final UserApiMapper userApiMapper;

    public UserController(UserRepositoryPort userRepositoryPort, UserApiMapper userApiMapper) {
        this.userRepositoryPort = userRepositoryPort;
        this.userApiMapper = userApiMapper;
    }

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable UUID id) {
        return userRepositoryPort.findById(id)
                .map(userApiMapper::toResponse)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserResponse createUser(@RequestBody CreateUserRequest request) {
        var user = userApiMapper.toCommand(request);
        var savedUser = userRepositoryPort.save(user);
        return userApiMapper.toResponse(savedUser);
    }
}
