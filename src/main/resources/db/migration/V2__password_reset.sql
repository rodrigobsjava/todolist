CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE password_reset_tokens (
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	user_id UUID NOT NULL,
	token_hash VARCHAR(64) NOT NULL,
	expires_at TIMESTAMPTZ NOT NULL,
	used_at TIMESTAMPTZ NULL,
	created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
	
	CONSTRAINT fk_password_reset_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
	
	CONSTRAINT chk_password_reset_expires_after_created CHECK (expires_at > created_at) 
	
);

CREATE INDEX idx_password_reset_user_id ON password_reset_tokens (user_id);

CREATE UNIQUE INDEX uq_password_reset_token_hash ON password_reset_tokens (token_hash);

CREATE INDEX idx_password_reset_expires_at ON password_reset_tokens (expires_at);

CREATE INDEX idx_password_reset_unused ON password_reset_tokens (user_id) WHERE used_at IS NULL;

