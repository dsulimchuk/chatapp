-- Create rooms table
CREATE TABLE rooms (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(500),
    created_at TIMESTAMP NOT NULL,
    created_by VARCHAR(255) NOT NULL
);

-- Add room_id column to messages table
ALTER TABLE messages ADD COLUMN room_id BIGINT;
ALTER TABLE messages ADD CONSTRAINT fk_message_room FOREIGN KEY (room_id) REFERENCES rooms (id);

-- Create default "General" room
INSERT INTO rooms (name, description, created_at, created_by) 
VALUES ('General', 'Public chat room for everyone', CURRENT_TIMESTAMP, 'System');

-- Assign all existing messages to the General room
UPDATE messages SET room_id = (SELECT id FROM rooms WHERE name = 'General');

-- Make room_id not nullable for future messages
ALTER TABLE messages ALTER COLUMN room_id SET NOT NULL;