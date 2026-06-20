package io.github.solvo.application.ports.out;

import io.github.solvo.domain.entities.Transfer;

public interface NotificationPort {
    
    void notifyTransfer(Transfer transfer);
}
