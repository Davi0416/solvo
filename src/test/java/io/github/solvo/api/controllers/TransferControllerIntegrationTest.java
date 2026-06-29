package io.github.solvo.api.controllers;

import io.github.solvo.IntegrationTestBase;
import io.github.solvo.api.dtos.CreateTransferRequest;
import io.github.solvo.api.dtos.CreateUserRequest;
import io.github.solvo.api.dtos.CreateWalletRequest;
import io.github.solvo.api.dtos.LoginRequest;
import io.github.solvo.api.dtos.LoginResponse;
import io.github.solvo.api.dtos.TransferResponse;
import io.github.solvo.api.dtos.UserResponse;
import io.github.solvo.api.dtos.WalletResponse;
import io.github.solvo.application.ports.out.AuthorizationServicePort;
import io.github.solvo.application.ports.out.WalletRepositoryPort;
import io.github.solvo.domain.enums.TransferStatus;
import io.github.solvo.domain.enums.UserType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TransferControllerIntegrationTest extends IntegrationTestBase {

    @MockitoBean
    private AuthorizationServicePort authorizationServicePort;

    @Autowired
    private WalletRepositoryPort walletRepositoryPort;

    private record UsuarioComCarteira(String email, String senha, WalletResponse wallet, HttpHeaders headers) {
    }

    private HttpHeaders authHeaders(String email, String senha) {
        var login = restTemplate.postForEntity("/api/v1/auth/login", new LoginRequest(email, senha), LoginResponse.class).getBody();
        var headers = new HttpHeaders();
        headers.setBearerAuth(login.token());
        return headers;
    }

    private UsuarioComCarteira criarUsuarioComCarteira(UserType userType, BigDecimal saldoInicial) {
        var email = userType + "-" + System.nanoTime() + "@example.com";
        var senha = "senha123";
        var user = restTemplate.postForEntity("/api/v1/users",
                new CreateUserRequest("Usuario " + userType, "12312312300", userType, email, senha, "11999999999"),
                UserResponse.class).getBody();

        var headers = authHeaders(email, senha);

        var wallet = restTemplate.postForEntity("/api/v1/wallets",
                new HttpEntity<>(new CreateWalletRequest(user.id(), userType), headers), WalletResponse.class).getBody();

        if (saldoInicial.compareTo(BigDecimal.ZERO) > 0) {
            var domainWallet = walletRepositoryPort.findById(wallet.id()).orElseThrow();
            domainWallet.receive(saldoInicial);
            walletRepositoryPort.save(domainWallet);
        }

        return new UsuarioComCarteira(email, senha, wallet, headers);
    }

    @Test
    void deveTransferirComSucessoQuandoAutorizado() {
        when(authorizationServicePort.authorize(any())).thenReturn(true);

        var sender = criarUsuarioComCarteira(UserType.CONSUMER, new BigDecimal("100.00"));
        var receiver = criarUsuarioComCarteira(UserType.CONSUMER, BigDecimal.ZERO);

        var request = new CreateTransferRequest(sender.wallet().id(), receiver.wallet().id(), new BigDecimal("30.00"));
        var entity = new HttpEntity<>(request, sender.headers());

        ResponseEntity<TransferResponse> response = restTemplate.postForEntity("/api/v1/transfers", entity, TransferResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().status()).isEqualTo(TransferStatus.APPROVED);

        var senderWallet = restTemplate.exchange("/api/v1/wallets/{id}", org.springframework.http.HttpMethod.GET,
                new HttpEntity<>(sender.headers()), WalletResponse.class, sender.wallet().id()).getBody();
        var receiverWallet = restTemplate.exchange("/api/v1/wallets/{id}", org.springframework.http.HttpMethod.GET,
                new HttpEntity<>(sender.headers()), WalletResponse.class, receiver.wallet().id()).getBody();

        assertThat(senderWallet.balance()).isEqualByComparingTo(new BigDecimal("70.00"));
        assertThat(receiverWallet.balance()).isEqualByComparingTo(new BigDecimal("30.00"));
    }

    @Test
    void deveRecusarTransferenciaQuandoServicoExternoNegaAutorizacao() {
        when(authorizationServicePort.authorize(any())).thenReturn(false);

        var sender = criarUsuarioComCarteira(UserType.CONSUMER, new BigDecimal("100.00"));
        var receiver = criarUsuarioComCarteira(UserType.CONSUMER, BigDecimal.ZERO);

        var request = new CreateTransferRequest(sender.wallet().id(), receiver.wallet().id(), new BigDecimal("30.00"));
        var entity = new HttpEntity<>(request, sender.headers());

        ResponseEntity<TransferResponse> response = restTemplate.postForEntity("/api/v1/transfers", entity, TransferResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().status()).isEqualTo(TransferStatus.REJECTED);
    }

    @Test
    void deveRecusarTransferenciaComSaldoInsuficiente() {
        when(authorizationServicePort.authorize(any())).thenReturn(true);

        var sender = criarUsuarioComCarteira(UserType.CONSUMER, new BigDecimal("10.00"));
        var receiver = criarUsuarioComCarteira(UserType.CONSUMER, BigDecimal.ZERO);

        var request = new CreateTransferRequest(sender.wallet().id(), receiver.wallet().id(), new BigDecimal("30.00"));
        var entity = new HttpEntity<>(request, sender.headers());

        ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/transfers", entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void deveRecusarTransferenciaDeLojista() {
        var merchant = criarUsuarioComCarteira(UserType.MERCHANT, new BigDecimal("100.00"));
        var receiver = criarUsuarioComCarteira(UserType.CONSUMER, BigDecimal.ZERO);

        var request = new CreateTransferRequest(merchant.wallet().id(), receiver.wallet().id(), new BigDecimal("30.00"));
        var entity = new HttpEntity<>(request, merchant.headers());

        ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/transfers", entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void deveDevolverMesmaRespostaParaChaveDeIdempotenciaRepetida() {
        when(authorizationServicePort.authorize(any())).thenReturn(true);

        var sender = criarUsuarioComCarteira(UserType.CONSUMER, new BigDecimal("100.00"));
        var receiver = criarUsuarioComCarteira(UserType.CONSUMER, BigDecimal.ZERO);
        sender.headers().add("Idempotency-Key", "chave-fixa-" + sender.wallet().id());

        var request = new CreateTransferRequest(sender.wallet().id(), receiver.wallet().id(), new BigDecimal("30.00"));
        var entity = new HttpEntity<>(request, sender.headers());

        var first = restTemplate.postForEntity("/api/v1/transfers", entity, TransferResponse.class).getBody();
        var second = restTemplate.postForEntity("/api/v1/transfers", entity, TransferResponse.class).getBody();

        assertThat(second.transferId()).isEqualTo(first.transferId());

        var senderWallet = restTemplate.exchange("/api/v1/wallets/{id}", org.springframework.http.HttpMethod.GET,
                new HttpEntity<>(sender.headers()), WalletResponse.class, sender.wallet().id()).getBody();
        assertThat(senderWallet.balance()).isEqualByComparingTo(new BigDecimal("70.00"));
    }
}
