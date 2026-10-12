CREATE SCHEMA transaction;

CREATE TABLE transaction.order(
    id bigint NOT NULL,
    order_number character varying NOT NULL,
    total_price bigint NOT NULL,
    cash_paid_by_customer bigint NOT NULL,
    change bigint,
    payment_method character varying,
    attachment BYTEA,
    status character varying,
    notes character varying,
    created_date timestamp with time zone DEFAULT now() NOT NULL,
    created_by character varying NOT NULL,
    modified_date timestamp with time zone,
    modified_by character varying,
    is_delete boolean NOT NULL
);

ALTER TABLE transaction.order
ADD CONSTRAINT order_number_unique UNIQUE (order_number);

CREATE SEQUENCE transaction.order_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE transaction.order_id_seq OWNED BY transaction.order.id;

ALTER TABLE ONLY transaction.order ALTER COLUMN id SET DEFAULT nextval('transaction.order_id_seq'::regclass);

SELECT pg_catalog.setval('transaction.order_id_seq', 1, false);

ALTER TABLE ONLY transaction.order
ADD CONSTRAINT order_pkey PRIMARY KEY (id);

CREATE TABLE transaction.order_item(
    id bigint NOT NULL,
    order_id bigint NOT NULL,
    product_id bigint NOT NULL,
    total_item integer NOT NULL,
    current_item_price bigint NOT NULL,
    created_date timestamp with time zone DEFAULT now() NOT NULL,
    created_by character varying NOT NULL,
    modified_date timestamp with time zone,
    modified_by character varying,
    is_delete boolean NOT NULL
);

CREATE SEQUENCE transaction.order_item_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE transaction.order_item_id_seq OWNED BY transaction.order_item.id;

ALTER TABLE ONLY transaction.order_item ALTER COLUMN id SET DEFAULT nextval('transaction.order_item_id_seq'::regclass);
SELECT pg_catalog.setval('transaction.order_item_id_seq', 1, false);

ALTER TABLE ONLY transaction.order_item
ADD CONSTRAINT order_item_pkey PRIMARY KEY (id);

ALTER TABLE ONLY transaction.order_item
ADD CONSTRAINT order_id_fk FOREIGN KEY (order_id) REFERENCES transaction.order(id);

ALTER TABLE ONLY transaction.order_item
ADD CONSTRAINT product_id_fk FOREIGN KEY (product_id) REFERENCES master_catalog.product(id);
