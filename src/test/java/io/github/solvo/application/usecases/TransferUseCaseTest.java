package io.github.solvo.application.usecases;

import io.github.solvo.application.ports.in.TransferCommand;
import io.github.solvo.application.ports.out.AuthorizationServicePort;
import io.github.solvo.application.ports.out.NotificationPort;
import io.github.solvo.application.ports.out.TransferRepositoryPort;
import io.github.solvo.application.ports.out.WalletRepositoryPort;
import io.github.solvo.domain.entities.Transfer;
import io.github.solvo.domain.entities.Wallet;
import io.github.solvo.domain.enums.TransferStatus;
import io.github.solvo.domain.enums.UserType;
import io.github.solvo.domain.exceptions.WalletNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferUseCaseTest {
    @Mock
    private AuthorizationServicePort authorizationServicePort;
    @Mock
    private NotificationPort notificationPort;
    @Mock
    private TransferRepositoryPort transferRepositoryPort;
    @Mock
    private WalletRepositoryPort walletRepositoryPort;

    @InjectMocks
    private TransferUseCase transferUseCase;

    private Wallet senderWallet;
    private Wallet receiverWallet;
    private Wallet merchant;

    @BeforeEach
    void setUp() {
        merchant = new Wallet(UUID.randomUUID(), UUID.randomUUID(), UserType.MERCHANT, new BigDecimal("1000.00"));
        senderWallet = new Wallet(UUID.randomUUID(), UUID.randomUUID(), UserType.CONSUMER, new BigDecimal("100.00"));
        receiverWallet = new Wallet(UUID.randomUUID(), UUID.randomUUID(), UserType.CONSUMER, new BigDecimal("50.00"));
    }

    @Test
    void deveFazerTransferenciaComSucesso() {
        UUID senderWalletId = senderWallet.getId();
        UUID receiverWalletId = receiverWallet.getId();
        BigDecimal transferValue = new BigDecimal("99.90");

        TransferCommand transferCommand = new TransferCommand(senderWalletId, receiverWalletId, transferValue);

        when(authorizationServicePort.authorize(ArgumentMatchers.any()))
                .thenReturn(true);
        when(walletRepositoryPort.findById(senderWalletId))
                .thenReturn(Optional.of(senderWallet));
        when(walletRepositoryPort.findById(receiverWalletId))
                .thenReturn(Optional.of(receiverWallet));

        transferUseCase.transfer(transferCommand);
        assertEquals(new BigDecimal("149.90"), receiverWallet.getBalance());
        assertEquals(new BigDecimal("0.10"), senderWallet.getBalance());
    }

    @Test
    void deveRecusarTransferenciaPorValor() {
        UUID senderWalletId = senderWallet.getId();
        UUID receiverWalletId = receiverWallet.getId();
        BigDecimal transferValue = new BigDecimal("300.00");

        TransferCommand transferCommand = new TransferCommand(senderWalletId, receiverWalletId, transferValue);

        when(walletRepositoryPort.findById(senderWalletId))
                .thenReturn(Optional.of(senderWallet));
        when(walletRepositoryPort.findById(receiverWalletId))
                .thenReturn(Optional.of(receiverWallet));

        assertThrows(IllegalArgumentException.class, () -> transferUseCase.transfer(transferCommand));
    }

    @Test
    void deveRecusarTransferenciaPorAutorizacao() {
        UUID senderWalletId = senderWallet.getId();
        UUID receiverWalletId = receiverWallet.getId();
        BigDecimal transferValue = new BigDecimal("50.00");

        TransferCommand transferCommand = new TransferCommand(senderWalletId, receiverWalletId, transferValue);

        when(authorizationServicePort.authorize(ArgumentMatchers.any()))
                .thenReturn(false);
        when(walletRepositoryPort.findById(senderWalletId))
                .thenReturn(Optional.of(senderWallet));
        when(walletRepositoryPort.findById(receiverWalletId))
                .thenReturn(Optional.of(receiverWallet));

        Transfer trasfer = transferUseCase.transfer(transferCommand);

        assertEquals(TransferStatus.REJECTED, trasfer.getStatus());
    }

    @Test
    void deveDarErroPorSerLojista() {
        UUID merchantWalletId = merchant.getId();
        UUID receiverWalletId = receiverWallet.getId();
        BigDecimal transferValue = new BigDecimal("150.00");

        TransferCommand transferCommand = new TransferCommand(merchantWalletId, receiverWalletId, transferValue);

        when(walletRepositoryPort.findById(merchantWalletId))
                .thenReturn(Optional.of(merchant));
        when(walletRepositoryPort.findById(receiverWalletId))
                .thenReturn(Optional.of(receiverWallet));

        assertThrows(IllegalArgumentException.class, () -> transferUseCase.transfer(transferCommand));
    }

    @Test
    void deveDarErroCarteiraNaoEncontrada() {
        when(walletRepositoryPort.findById(ArgumentMatchers.any()))
                .thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () -> transferUseCase.transfer(new TransferCommand(UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("100.00"))));
    }
}
