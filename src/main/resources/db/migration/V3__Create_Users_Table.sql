-- Create users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    display_name VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE
);

-- Create default admin user
INSERT INTO users (username, password, display_name, enabled)
VALUES ('admin', '$2a$10$8MeF8HAXCl.YjJkhG/K2jufCKJHPGjn4qgHKXvzTxhx3O4QJbOTie', 'Administrator', true);

-- Update existing messages to associate with the admin user
ALTER TABLE messages ADD COLUMN user_id BIGINT;
UPDATE messages SET user_id = (SELECT id FROM users WHERE username = 'admin');
ALTER TABLE messages ALTER COLUMN user_id SET NOT NULL;
ALTER TABLE messages ADD CONSTRAINT fk_message_user FOREIGN KEY (user_id) REFERENCES users (id);