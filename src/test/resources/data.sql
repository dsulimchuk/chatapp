-- Insert a default room for testing
INSERT INTO rooms (id, name, description, created_at, created_by)
VALUES (1, 'Test Room', 'A room for testing', CURRENT_TIMESTAMP, 'System');

-- Ensure ID sequence is updated (for PostgreSQL)
-- This isn't needed for H2 with in-memory DB, but it's good to include for completeness
ALTER SEQUENCE IF EXISTS rooms_id_seq RESTART WITH 2;