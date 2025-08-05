CREATE TABLE trendbar_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    symbol_id BIGINT NOT NULL,
    volume BIGINT NOT NULL,
    period INT NOT NULL,
    low_price DECIMAL(19,5) NOT NULL,
    open_price DECIMAL(19,5) NOT NULL,
    close_price DECIMAL(19,5) NOT NULL,
    high_price DECIMAL(19,5) NOT NULL,
    utc_timestamp_in_minutes BIGINT NOT NULL,
    CONSTRAINT fk_trendbar_symbol FOREIGN KEY (symbol_id) REFERENCES symbols(id) ON DELETE CASCADE
);

CREATE INDEX idx_trendbar_timestamp ON trendbar_history(utc_timestamp_in_minutes);