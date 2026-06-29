package io.github.solvo.api.controllers;

import io.github.solvo.IntegrationTestBase;
import io.github.solvo.api.dtos.CreateUserRequest;
import io.github.solvo.api.dtos.LoginRequest;
import io.github.solvo.api.dtos.LoginResponse;
import io.github.solvo.api.dtos.UserResponse;
import io.github.solvo.domain.enums.UserType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserControllerIntegrationTest extends IntegrationTestBase {

    @Test
    void deveCadastrarUsuarioSemExporSenha() {
        var request = new CreateUserRequest("Maria Silva", "12345678900", UserType.CONSUMER,
                "maria-" + System.nanoTime() + "@example.com", "senha123", "11999999999");

        ResponseEntity<UserResponse> response = restTemplate.postForEntity("/api/v1/users", request, UserResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().email()).isEqualTo(request.email());
        assertThat(response.getBody().id()).isNotNull();
    }

    @Test
    void deveBuscarUsuarioPorId() {
        var email = "joao-" + System.nanoTime() + "@example.com";
        var senha = "senha456";
        var request = new CreateUserRequest("Joao Souza", "98765432100", UserType.MERCHANT, email, senha, "11988888888");
        var created = restTemplate.postForEntity("/api/v1/users", request, UserResponse.class).getBody();

        var login = restTemplate.postForEntity("/api/v1/auth/login", new LoginRequest(email, senha), LoginResponse.class).getBody();
        var headers = new org.springframework.http.HttpHeaders();
        headers.setBearerAuth(login.token());

        ResponseEntity<UserResponse> response = restTemplate.exchange("/api/v1/users/{id}", HttpMethod.GET,
                new HttpEntity<>(headers), UserResponse.class, created.id());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().name()).isEqualTo("Joao Souza");
        assertThat(response.getBody().userType()).isEqualTo(UserType.MERCHANT);
    }

    @Test
    void deveRetornar404QuandoUsuarioNaoExisteParaQuemEstaAutenticado() {
        var email = "auth-404-" + System.nanoTime() + "@example.com";
        var senha = "senha789";
        restTemplate.postForEntity("/api/v1/users",
                new CreateUserRequest("Usuario Auth", "11111111111", UserType.CONSUMER, email, senha, "11900000000"),
                UserResponse.class);

        var login = restTemplate.postForEntity("/api/v1/auth/login", new LoginRequest(email, senha), LoginResponse.class).getBody();
        var headers = new org.springframework.http.HttpHeaders();
        headers.setBearerAuth(login.token());

        ResponseEntity<String> response = restTemplate.exchange("/api/v1/users/{id}", HttpMethod.GET,
                new HttpEntity<>(headers), String.class, UUID.randomUUID());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deveBloquearBuscaSemAutenticacao() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/users/{id}", String.class, UUID.randomUUID());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}
