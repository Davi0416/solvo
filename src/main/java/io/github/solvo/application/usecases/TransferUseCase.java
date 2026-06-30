package io.github.solvo.application.usecases;

import io.github.solvo.application.ports.in.TransferCommand;
import io.github.solvo.application.ports.in.TransferUseCasePort;
import io.github.solvo.application.ports.out.AuthorizationServicePort;
import io.github.solvo.application.ports.out.NotificationPort;
import io.github.solvo.application.ports.out.TransferRepositoryPort;
import io.github.solvo.application.ports.out.WalletRepositoryPort;
import io.github.solvo.domain.entities.Transfer;
import io.github.solvo.domain.entities.Wallet;
import io.github.solvo.domain.enums.TransferStatus;
import io.github.solvo.domain.exceptions.WalletNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransferUseCase implements TransferUseCasePort {

    private final WalletRepositoryPort walletRepositoryPort;
    private final TransferRepositoryPort transferRepositoryPort;
    private final AuthorizationServicePort authorizationServicePort;
    private final NotificationPort notificationPort;

    public TransferUseCase(WalletRepositoryPort walletRepositoryPort,
                           TransferRepositoryPort transferRepositoryPort,
                           AuthorizationServicePort authorizationServicePort,
                           NotificationPort notificationPort) {
        this.walletRepositoryPort = walletRepositoryPort;
        this.transferRepositoryPort = transferRepositoryPort;
        this.authorizationServicePort = authorizationServicePort;
        this.notificationPort = notificationPort;
    }

    @Override
    @Transactional
    public Transfer transfer(TransferCommand command) {
        Wallet senderWallet = walletRepositoryPort.findByIdForUpdate(command.senderWalletId())
                .orElseThrow(() -> new WalletNotFoundException(command.senderWalletId()));
        Wallet receiverWallet = walletRepositoryPort.findByIdForUpdate(command.receiverWalletId())
                .orElseThrow(() -> new WalletNotFoundException(command.receiverWalletId()));

        Transfer transfer = senderWallet.transfer(command.transferValue(), receiverWallet.getId());

        transfer.processAuthorization(authorizationServicePort.authorize(transfer));

        if (transfer.getStatus() == TransferStatus.REJECTED) {
            senderWallet.refund(transfer.getAmount());
            walletRepositoryPort.save(senderWallet);
            return transferRepositoryPort.save(transfer);
        }

        receiverWallet.receive(transfer.getAmount());
        walletRepositoryPort.save(receiverWallet);
        walletRepositoryPort.save(senderWallet);
        Transfer savedTransfer = transferRepositoryPort.save(transfer);
        notificationPort.notifyTransfer(savedTransfer);

        return savedTransfer;
    }
}
