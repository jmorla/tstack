-- ========================================
-- Flyway Migration: V4__Add_download_progress_to_dataset_metadata.sql
-- Description: Add download_progress field to dataset_metadata table
-- Author: Market Data Team
-- Date: 2025-09-06
-- ========================================

-- Add download_progress column to dataset_metadata table
ALTER TABLE dataset_metadata 
ADD COLUMN download_progress DECIMAL(5,2) DEFAULT 0.00;

-- ========================================
-- End of Migration
-- ========================================