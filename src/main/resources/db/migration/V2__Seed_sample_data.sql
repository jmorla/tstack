-- ========================================
-- Flyway Migration: V2__Seed_sample_data.sql
-- Description: Seed tables with sample data for testing and development
-- Author: Market Data Team
-- Date: 2025-08-19
-- ========================================

-- Insert sample providers
INSERT INTO providers (id, name, description, created_at, updated_at) VALUES
(1, 'Dukascopy', 'Swiss forex data provider offering high-quality historical and live market data', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'MetaTrader', 'Popular forex and CFD trading platform with comprehensive market data', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Yahoo Finance', 'Comprehensive financial data provider for stocks, forex, and commodities', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Reset sequence to ensure proper auto-increment
SELECT setval('providers_id_seq', 3);

-- Insert sample instruments
INSERT INTO instruments (id, provider_id, platform_id, symbol, name, description, created_at, updated_at) VALUES
-- Dukascopy forex instruments
(1, 1, 'DUK_001', 'EURUSD', 'Euro vs US Dollar', 'Major forex pair - Euro against US Dollar', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 1, 'DUK_002', 'GBPUSD', 'British Pound vs US Dollar', 'Major forex pair - British Pound against US Dollar', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 1, 'DUK_003', 'USDJPY', 'US Dollar vs Japanese Yen', 'Major forex pair - US Dollar against Japanese Yen', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- MetaTrader forex instruments
(4, 2, 'MT5_001', 'EURUSD', 'Euro vs US Dollar', 'EUR/USD forex pair on MetaTrader 5 platform', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 2, 'MT5_002', 'BTCUSD', 'Bitcoin vs US Dollar', 'Bitcoin cryptocurrency against US Dollar', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Yahoo Finance stocks
(6, 3, 'NASDAQ_AAPL', 'AAPL', 'Apple Inc.', 'Apple Inc. common stock', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 3, 'NASDAQ_GOOGL', 'GOOGL', 'Alphabet Inc.', 'Alphabet Inc. Class A common stock', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 3, 'NASDAQ_TSLA', 'TSLA', 'Tesla Inc.', 'Tesla Inc. common stock', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Additional crypto
(9, 2, 'MT5_003', 'ETHUSD', 'Ethereum vs US Dollar', 'Ethereum cryptocurrency against US Dollar', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Reset sequence for instruments
SELECT setval('instruments_id_seq', 9);

-- Insert dataset metadata for each instrument
INSERT INTO dataset_metadata (id, instrument_id, total_records, start_date, end_date, dataset_size_bytes, status, created_at, updated_at) VALUES
-- Dukascopy instruments (high frequency data)
(1, 1, 87840, '2025-06-01 00:00:00', '2025-08-19 23:59:00', 25165824, 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 2, 83520, '2025-06-01 00:00:00', '2025-08-19 23:59:00', 23887872, 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 3, 89280, '2025-06-01 00:00:00', '2025-08-19 23:59:00', 25518080, 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- MetaTrader instruments
(4, 4, 52560, '2025-07-01 00:00:00', '2025-08-19 23:59:00', 15032320, 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 5, 71280, '2025-06-15 00:00:00', '2025-08-19 23:59:00', 20388864, 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Yahoo Finance stocks (market hours only)
(6, 6, 19800, '2025-07-01 09:30:00', '2025-08-19 16:00:00', 5664000, 'COMPLETED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 7, 19800, '2025-07-01 09:30:00', '2025-08-19 16:00:00', 5664000, 'COMPLETED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 8, 19350, '2025-07-01 09:30:00', '2025-08-19 16:00:00', 5535360, 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Ethereum
(9, 9, 68400, '2025-06-20 00:00:00', '2025-08-19 23:59:00', 19562496, 'PENDING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Reset sequence for dataset_metadata
SELECT setval('dataset_metadata_id_seq', 9);

-- Insert sample price data (last 2 days for demonstration)
-- EURUSD sample data (Dukascopy)
INSERT INTO price_data (instrument_id, timestamp, open_price, high_price, low_price, close_price, volume) VALUES
-- August 18, 2025 - Sample EURUSD 1-minute bars
(1, '2025-08-18 09:00:00', 1.08450, 1.08465, 1.08442, 1.08458, 1250000.00),
(1, '2025-08-18 09:01:00', 1.08458, 1.08472, 1.08445, 1.08467, 980000.00),
(1, '2025-08-18 09:02:00', 1.08467, 1.08479, 1.08461, 1.08475, 1100000.00),
(1, '2025-08-18 09:03:00', 1.08475, 1.08481, 1.08469, 1.08473, 850000.00),
(1, '2025-08-18 09:04:00', 1.08473, 1.08487, 1.08470, 1.08484, 1300000.00),

-- August 19, 2025 - Recent EURUSD data
(1, '2025-08-19 08:00:00', 1.08520, 1.08535, 1.08515, 1.08528, 1450000.00),
(1, '2025-08-19 08:01:00', 1.08528, 1.08542, 1.08521, 1.08536, 1200000.00),
(1, '2025-08-19 08:02:00', 1.08536, 1.08548, 1.08530, 1.08545, 1050000.00),

-- AAPL sample data (Yahoo Finance)
(6, '2025-08-18 09:30:00', 224.15, 224.87, 223.92, 224.58, 485200.00),
(6, '2025-08-18 09:31:00', 224.58, 224.95, 224.41, 224.73, 328900.00),
(6, '2025-08-18 09:32:00', 224.73, 225.12, 224.68, 224.95, 412300.00),
(6, '2025-08-18 09:33:00', 224.95, 225.08, 224.79, 224.86, 298600.00),

-- BTCUSD sample data (MetaTrader)
(5, '2025-08-18 10:00:00', 61250.50, 61387.25, 61198.75, 61345.80, 12.45680000),
(5, '2025-08-18 10:01:00', 61345.80, 61425.60, 61312.40, 61398.20, 8.78950000),
(5, '2025-08-18 10:02:00', 61398.20, 61456.90, 61375.10, 61442.15, 15.32180000),

-- TSLA sample data
(8, '2025-08-19 09:30:00', 238.45, 239.12, 237.88, 238.67, 892400.00),
(8, '2025-08-19 09:31:00', 238.67, 239.25, 238.34, 238.95, 654300.00),
(8, '2025-08-19 09:32:00', 238.95, 239.48, 238.76, 239.22, 558900.00);

-- ========================================
-- End of Migration
-- ========================================