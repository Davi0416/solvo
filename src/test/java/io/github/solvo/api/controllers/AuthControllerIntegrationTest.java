package io.github.solvo.api.controllers;

import io.github.solvo.IntegrationTestBase;
import io.github.solvo.api.dtos.CreateUserRequest;
import io.github.solvo.api.dtos.LoginRequest;
import io.github.solvo.api.dtos.LoginResponse;
import io.github.solvo.domain.enums.UserType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class AuthControllerIntegrationTest extends IntegrationTestBase {

    @Test
    void deveAutenticarComCredenciaisValidasERetornarToken() {
        var email = "login-" + System.nanoTime() + "@example.com";
        restTemplate.postForEntity("/api/v1/users",
                new CreateUserRequest("Login Teste", "55566677788", UserType.CONSUMER, email, "senhaCorreta", "11977777777"),
                Void.class);

        ResponseEntity<LoginResponse> response = restTemplate.postForEntity("/api/v1/auth/login",
                new LoginRequest(email, "senhaCorreta"), LoginResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().token()).isNotBlank();
    }

    @Test
    void deveRecusarLoginComSenhaIncorreta() {
        var email = "login-invalido-" + System.nanoTime() + "@example.com";
        restTemplate.postForEntity("/api/v1/users",
                new CreateUserRequest("Login Invalido", "66677788899", UserType.CONSUMER, email, "senhaCorreta", "11966666666"),
                Void.class);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/auth/login",
                new LoginRequest(email, "senhaErrada"), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void deveBloquearAcessoAEndpointProtegidoSemToken() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/wallets/{id}", String.class,
                "00000000-0000-0000-0000-000000000000");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}
