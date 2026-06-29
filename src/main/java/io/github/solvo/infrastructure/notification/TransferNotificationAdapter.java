package io.github.solvo.infrastructure.notification;

import io.github.solvo.application.ports.out.NotificationPort;
import io.github.solvo.domain.entities.Transfer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TransferNotificationAdapter implements NotificationPort {

    private final KafkaTemplate<String, TransferCompletedEvent> kafkaTemplate;
    private final String topic;

    public TransferNotificationAdapter(KafkaTemplate<String, TransferCompletedEvent> kafkaTemplate,
                                        @Value("${kafka.topics.transfer-completed}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void notifyTransfer(Transfer transfer) {
        var event = new TransferCompletedEvent(
                transfer.getTransferId(),
                transfer.getSenderWalletId(),
                transfer.getReceiverWalletId(),
                transfer.getAmount()
        );

        kafkaTemplate.send(topic, transfer.getTransferId().toString(), event);
    }
}

