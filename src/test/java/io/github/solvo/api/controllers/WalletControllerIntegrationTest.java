package io.github.solvo.api.controllers;

import io.github.solvo.IntegrationTestBase;
import io.github.solvo.api.dtos.CreateUserRequest;
import io.github.solvo.api.dtos.CreateWalletRequest;
import io.github.solvo.api.dtos.LoginRequest;
import io.github.solvo.api.dtos.LoginResponse;
import io.github.solvo.api.dtos.UserResponse;
import io.github.solvo.api.dtos.WalletResponse;
import io.github.solvo.domain.enums.UserType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class WalletControllerIntegrationTest extends IntegrationTestBase {

    private record UsuarioAutenticado(UserResponse user, HttpHeaders headers) {
    }

    private UsuarioAutenticado criarUsuario(UserType userType) {
        var email = userType + "-" + System.nanoTime() + "@example.com";
        var senha = "senha123";
        var request = new CreateUserRequest("Usuario Teste", "11122233344", userType, email, senha, "11999999999");
        var user = restTemplate.postForEntity("/api/v1/users", request, UserResponse.class).getBody();

        var login = restTemplate.postForEntity("/api/v1/auth/login", new LoginRequest(email, senha), LoginResponse.class).getBody();
        var headers = new HttpHeaders();
        headers.setBearerAuth(login.token());

        return new UsuarioAutenticado(user, headers);
    }

    @Test
    void deveCriarCarteiraComSaldoZerado() {
        var usuario = criarUsuario(UserType.CONSUMER);
        var request = new CreateWalletRequest(usuario.user().id(), UserType.CONSUMER);

        ResponseEntity<WalletResponse> response = restTemplate.postForEntity("/api/v1/wallets",
                new HttpEntity<>(request, usuario.headers()), WalletResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().balance()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(response.getBody().userId()).isEqualTo(usuario.user().id());
    }

    @Test
    void deveBuscarCarteiraPorIdUsandoCache() {
        var usuario = criarUsuario(UserType.MERCHANT);
        var created = restTemplate.postForEntity("/api/v1/wallets",
                new HttpEntity<>(new CreateWalletRequest(usuario.user().id(), UserType.MERCHANT), usuario.headers()),
                WalletResponse.class).getBody();

        var getEntity = new HttpEntity<Void>(usuario.headers());
        ResponseEntity<WalletResponse> first = restTemplate.exchange("/api/v1/wallets/{id}", HttpMethod.GET, getEntity, WalletResponse.class, created.id());
        ResponseEntity<WalletResponse> second = restTemplate.exchange("/api/v1/wallets/{id}", HttpMethod.GET, getEntity, WalletResponse.class, created.id());

        assertThat(first.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(second.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(second.getBody().balance()).isEqualByComparingTo(first.getBody().balance());
    }
}
