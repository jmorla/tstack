-- ========================================
-- Flyway Migration: V3__Add_pepperstone_provider.sql
-- Description: Add Pepperstone (cTrader) as a provider
-- Author: Market Data Team
-- Date: 2025-08-22
-- ========================================

-- Insert Pepperstone (cTrader) provider
INSERT INTO providers (name, description, created_at, updated_at) VALUES
('Pepperstone (cTrader)', 'Pepperstone forex and CFD broker using cTrader platform for market data and trading', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ========================================
-- End of Migration
-- ========================================