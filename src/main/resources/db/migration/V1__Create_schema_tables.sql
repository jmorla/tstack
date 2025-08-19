-- ========================================
-- Flyway Migration: V1__Create_market_data_tables.sql
-- Description: Create initial schema for market history price dataset
-- Author: Market Data Team
-- Date: 2025-08-19
-- ========================================

-- Provider table for data sources
CREATE TABLE providers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Instruments table
CREATE TABLE instruments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    provider_id BIGINT NOT NULL,
    platform_id VARCHAR(50) NOT NULL,
    symbol VARCHAR(20) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Foreign key to providers
    CONSTRAINT fk_instruments_provider FOREIGN KEY (provider_id) REFERENCES providers(id),

    -- Unique constraint per provider
    CONSTRAINT uk_provider_platform_symbol UNIQUE (provider_id, platform_id, symbol)
);

-- Create indexes for instruments table
CREATE INDEX idx_symbol ON instruments(symbol);
CREATE INDEX idx_provider ON instruments(provider_id);

-- Dataset metadata table (one-to-one with instruments)
CREATE TABLE dataset_metadata (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    instrument_id BIGINT NOT NULL UNIQUE,
    total_records BIGINT NOT NULL DEFAULT 0,
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    dataset_size_bytes BIGINT,
    status VARCHAR(20) NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Foreign key to instruments (one-to-one)
    CONSTRAINT fk_dataset_metadata_instrument FOREIGN KEY (instrument_id) REFERENCES instruments(id) ON DELETE CASCADE
);


-- Price data table for 1-minute OHLCV data
CREATE TABLE price_data (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    instrument_id BIGINT NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    open_price DECIMAL(20,8) NOT NULL,
    high_price DECIMAL(20,8) NOT NULL,
    low_price DECIMAL(20,8) NOT NULL,
    close_price DECIMAL(20,8) NOT NULL,
    volume DECIMAL(20,8) NOT NULL,

    -- Foreign key to instruments
    CONSTRAINT fk_price_data_instrument FOREIGN KEY (instrument_id) REFERENCES instruments(id),

    -- Unique constraint to prevent duplicate data points
    CONSTRAINT uk_price_data UNIQUE (instrument_id, timestamp)
);

-- Create indexes for price_data table
CREATE INDEX idx_price_instrument_time ON price_data(instrument_id, timestamp);
CREATE INDEX idx_timestamp ON price_data(timestamp);

-- ========================================
-- End of Migration
-- ========================================