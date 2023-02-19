CREATE TABLE USERS
(
    id SERIAL PRIMARY KEY NOT NULL UNIQUE ,
    username CHARACTER VARYING (55) NOT NULL UNIQUE,
    password CHARACTER VARYING (255) NOT NULL,
    email CHARACTER VARYING (55) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT now()
);

INSERT INTO USERS (username, password, email)
VALUES ('Mikolaj', 'last123', 'example@gmail.com');
INSERT INTO USERS (username, password, email)
VALUES ('Sofiya', 'test123', 'sone4ka@gmail.com');