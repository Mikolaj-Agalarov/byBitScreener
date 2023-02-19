CREATE TABLE TICKER_NAMES
(
    id SERIAL PRIMARY KEY NOT NULL UNIQUE,
    ticker_name CHARACTER VARYING (25) UNIQUE,
    min_order_value DECIMAL DEFAULT 30000,
    is_on boolean DEFAULT '1',
    vol DECIMAL,
    vol_value DECIMAL
);
