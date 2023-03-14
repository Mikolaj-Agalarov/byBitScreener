CREATE TABLE ASKS
(
    dom_id INTEGER,
    price DECIMAL,
    amount DECIMAL,
    range DECIMAL,
    percentage_of_daily_volume DECIMAL,
    volume_in_usd DECIMAL,

    FOREIGN KEY (dom_id) REFERENCES DOMS (id)
);
