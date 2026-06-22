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
    public Transfer transfer(TransferCommand command) {
        Wallet senderWallet = walletRepositoryPort.findByWalletId(command.senderWalletId())
                .orElseThrow(() -> new RuntimeException("Carteira de origem não encontrada"));
        Wallet receiverWallet = walletRepositoryPort.findByWalletId(command.receiverWalletId())
                .orElseThrow(() -> new RuntimeException("Carteira de destino não encontrada"));

        Transfer transfer = senderWallet.transfer(command.transferValue(), receiverWallet.getId());

        transfer.processAuthorization(authorizationServicePort.authorize(transfer));

        if (transfer.getStatus() == TransferStatus.REJECTED) {
            senderWallet.refund(transfer.getValue());
            walletRepositoryPort.save(senderWallet);
            transferRepositoryPort.save(transfer);
            return transfer;
        }

        receiverWallet.receive(transfer.getValue());
        walletRepositoryPort.save(receiverWallet);
        walletRepositoryPort.save(senderWallet);
        transferRepositoryPort.save(transfer);
        notificationPort.notifyTransfer(transfer);

        return transfer;
    }
}
