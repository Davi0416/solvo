CREATE TABLE transfers
(
    id                 UUID PRIMARY KEY,
    sender_wallet_id   UUID           NOT NULL REFERENCES wallets (id),
    receiver_wallet_id UUID           NOT NULL REFERENCES wallets (id),
    amount             DECIMAL(10, 2) NOT NULL,
    status             VARCHAR(20)    NOT NULL DEFAULT 'PENDING',
    created_at         TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
)
