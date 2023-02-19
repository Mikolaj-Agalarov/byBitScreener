CREATE TABLE DOMS
(
    id SERIAL PRIMARY KEY NOT NULL UNIQUE,
    ticker_id INTEGER,
    highest_bid_price CHARACTER VARYING (55),
    lowest_ask_price CHARACTER VARYING (55),

    FOREIGN KEY (ticker_id) REFERENCES TICKER_NAMES(id)
);
