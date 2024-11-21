-- Als Postgres User:
create user mtcg PASSWORD 'mtcg';
create database mtcg with owner baris;


-- Als MTCG db user:
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    token VARCHAR(255) UNIQUE
);

INSERT INTO users (username, password) VALUES ('testuser', 'testpassword');
INSERT INTO users (username, password) VALUES ('testuser2', '1234');
INSERT INTO users (username, password) VALUES ('testuser3', 'abcd');




