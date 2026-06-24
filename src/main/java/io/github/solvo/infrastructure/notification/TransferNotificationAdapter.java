package io.github.solvo.infrastructure.notification;

import io.github.solvo.application.ports.out.NotificationPort;
import io.github.solvo.domain.entities.Transfer;
import org.springframework.stereotype.Component;

@Component
public class TransferNotificationAdapter implements NotificationPort {

    @Override
    public void notifyTransfer(Transfer transfer) {
        // No-op implementation until a real notification provider is integrated.
    }
}

