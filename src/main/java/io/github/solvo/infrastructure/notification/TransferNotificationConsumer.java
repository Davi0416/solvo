package io.github.solvo.infrastructure.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransferNotificationConsumer {

    private static final Logger log = LoggerFactory.getLogger(TransferNotificationConsumer.class);

    @KafkaListener(topics = "${kafka.topics.transfer-completed}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(TransferCompletedEvent event) {
        log.info("Notificando carteira {} sobre recebimento de {} (transferencia {})",
                event.receiverWalletId(), event.amount(), event.transferId());
    }
}
