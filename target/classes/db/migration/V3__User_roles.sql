CREATE TABLE USER_ROLES
(
    user_id INTEGER,
    role_id INTEGER,
    FOREIGN KEY (user_id) REFERENCES USERS (id),
    FOREIGN KEY (role_id) REFERENCES ROLES (id),

    UNIQUE (user_id, role_id)
);

INSERT INTO USER_ROLES (user_id, role_id) VALUES (1, 1);
INSERT INTO USER_ROLES (user_id, role_id) VALUES (2, 2);