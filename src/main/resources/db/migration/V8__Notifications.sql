CREATE TABLE NOTIFICATIONS
(
    id SERIAL PRIMARY KEY NOT NULL UNIQUE,
    dom_id INTEGER,
    ticker_name CHARACTER VARYING (25),
    price DECIMAL NOT NULL,
    value DECIMAL NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    is_sk boolean,
    is_bid boolean,

    FOREIGN KEY (dom_id) REFERENCES DOMS (id),
    FOREIGN KEY (ticker_name) REFERENCES TICKER_NAMES (ticker_name)
);