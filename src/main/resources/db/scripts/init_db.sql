CREATE TABLE IF NOT EXISTS activation_tokens
(
    account_id    VARCHAR(40) NOT NULL,
    token         VARCHAR(40) NOT NULL,
    creation_date TIMESTAMP,
    last_modified TIMESTAMP,
    PRIMARY KEY (account_id)
);