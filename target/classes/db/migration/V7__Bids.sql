CREATE TABLE BIDS
(
    dom_id INTEGER,
    price DECIMAL,
    amount DECIMAL,
    range DECIMAL,

    FOREIGN KEY (dom_id) REFERENCES DOMS (id)
);
