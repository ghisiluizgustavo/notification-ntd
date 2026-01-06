CREATE TABLE notification (
    id SERIAL NOT NULL PRIMARY KEY,
    category VARCHAR NOT NULL,
    type VARCHAR NOT NULL,
    content TEXT NOT NULL,
    status VARCHAR NOT NULL,
    user_id INTEGER,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);