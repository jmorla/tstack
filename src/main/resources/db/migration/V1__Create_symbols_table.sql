CREATE TABLE symbols (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    platform_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    digits INT NOT NULL,
    description VARCHAR(500),
    enabled BOOLEAN DEFAULT TRUE,
    short_selling_enabled BOOLEAN DEFAULT FALSE,
    swap_long DECIMAL(19,5),
    swap_short DECIMAL(19,5),
    max_volume BIGINT,
    min_volume BIGINT,
    step_volume BIGINT,
    max_exposure BIGINT,
    schedule_time_zone VARCHAR(100),
    measurement_units VARCHAR(50),
    min_commission BIGINT
);

CREATE INDEX idx_symbols_platform_id ON symbols(platform_id);
CREATE INDEX idx_symbols_name ON symbols(name);