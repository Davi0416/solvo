CREATE TABLE wallets
(
    id         UUID PRIMARY KEY,
    user_id    UUID           NOT NULL REFERENCES users (id),
    balance    DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    user_type  VARCHAR(20),
    created_at TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
)
