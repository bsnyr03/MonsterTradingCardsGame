-- Als Postgres User:
create
user mtcg PASSWORD 'mtcg';
create
database mtcg with owner baris;


/* Create Table users */
CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255)       NOT NULL,
    token    VARCHAR(255) UNIQUE
);

/* Add some test users */
INSERT INTO users (username, password)
VALUES ('testuser', 'testpassword');
INSERT INTO users (username, password)
VALUES ('testuser2', '1234');

/* Create Table cards */
CREATE TABLE cards
(
    id      VARCHAR(36) PRIMARY KEY,
    name    VARCHAR(255)     NOT NULL,
    damage  DOUBLE PRECISION NOT NULL,
    element VARCHAR(50)      NOT NULL,
    type    VARCHAR(50)      NOT NULL,
    mtype VARCHAR(50) NOT NULL
);

/* Add some test cards */
INSERT INTO cards (name, damage, element, type, mtype) VALUES ('FireGoblin', '50', 'FIRE', 'MONSTER', 'GOBLIN');
INSERT INTO cards (name, damage, element, type, mtype) VALUES ('WaterSpell', '40', 'WATER', 'SPELL', 'NOMONSTER');

/* Create Table packages */
CREATE TABLE packages (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          sold BOOLEAN DEFAULT FALSE NOT NULL,
                          cards JSONB NOT NULL
);

/* Add foreign key to packages table */
ALTER TABLE packages ADD COLUMN user_id INTEGER REFERENCES users(id);

/* Add coins to users */
ALTER TABLE users ADD COLUMN coins INTEGER DEFAULT 20 NOT NULL;

/* Add elo to users */
ALTER TABLE users ADD COLUMN elo INTEGER DEFAULT 1000;

/* Add games played to users */
ALTER TABLE users ADD COLUMN games_played INTEGER DEFAULT 0;

/* Create Table decks */
CREATE TABLE decks (
                       id SERIAL PRIMARY KEY,
                       user_id INTEGER NOT NULL REFERENCES users(id),
                       cards JSONB NOT NULL
);

/* Empty Tabels */
DELETE FROM users;
DELETE FROM cards;
DELETE FROM packages;
DELETE FROM decks;

/* Reset Tabels */
TRUNCATE TABLE users RESTART IDENTITY;
TRUNCATE TABLE cards RESTART IDENTITY;
TRUNCATE TABLE packages RESTART IDENTITY;
TRUNCATE TABLE decks RESTART IDENTITY;

/* Reset Sequenzes */
ALTER SEQUENCE cards_id_seq RESTART WITH 1;
ALTER SEQUENCE packages_id_seq RESTART WITH 1;
ALTER SEQUENCE decks_id_seq RESTART WITH 1;
ALTER SEQUENCE users_id_seq RESTART WITH 1;

/* Reset Coins */
UPDATE users
SET coins = 20;

/* Reset ELO */
UPDATE users
SET elo = 1000;

/* Reset games played */
UPDATE users
SET games_played = 0;

/* Reset wins */
UPDATE users
SET wins = 0;

/* Reset losses */
UPDATE users
SET losses = 0;

/* Reset draws */
UPDATE users
SET draws = 0;

/* Testing */
SELECT * FROM users;
SELECT * FROM cards;
SELECT * FROM packages;
SELECT * FROM decks;

/* Add wins, losses, draws */
ALTER TABLE users ADD COLUMN wins INTEGER DEFAULT 0;
ALTER TABLE users ADD COLUMN losses INTEGER DEFAULT 0;
ALTER TABLE users ADD COLUMN draws INTEGER DEFAULT 0;