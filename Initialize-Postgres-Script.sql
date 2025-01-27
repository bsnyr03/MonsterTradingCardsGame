-- Als Postgres User:
create
user mtcg PASSWORD 'mtcg';
create
database mtcg with owner baris;


-- Als MTCG db user:
CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255)       NOT NULL,
    token    VARCHAR(255) UNIQUE
);

INSERT INTO users (username, password)
VALUES ('testuser', 'testpassword');
INSERT INTO users (username, password)
VALUES ('testuser2', '1234');
INSERT INTO users (username, password)
VALUES ('testuser3', 'abcd');


CREATE TABLE cards
(
    id      VARCHAR(36) PRIMARY KEY,
    name    VARCHAR(255)     NOT NULL,
    damage  DOUBLE PRECISION NOT NULL,
    element VARCHAR(50)      NOT NULL,
    type    VARCHAR(50)      NOT NULL
);

INSERT INTO cards (name, damage, element, type) VALUES ('FireGoblin', '50', 'FIRE', 'MONSTER');
INSERT INTO cards (name, damage, element, type) VALUES ('WaterSpell', '40', 'WATER', 'SPELL');

CREATE TABLE packages (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          sold BOOLEAN DEFAULT FALSE NOT NULL,
                          cards JSONB NOT NULL
);

ALTER TABLE packages ADD COLUMN user_id INTEGER REFERENCES users(id);

ALTER TABLE users ADD COLUMN coins INTEGER DEFAULT 20 NOT NULL;

CREATE TABLE decks (
                       id SERIAL PRIMARY KEY,
                       user_id INTEGER NOT NULL REFERENCES users(id),
                       cards JSONB NOT NULL
);


/* Tabellen leeren */
DELETE FROM users;
DELETE FROM cards;
DELETE FROM packages;

/* Tabellen zurücksetzen */
TRUNCATE TABLE cards RESTART IDENTITY;
TRUNCATE TABLE packages RESTART IDENTITY;

/* Coins zurücksetzen */
UPDATE users
SET coins = 20;