CREATE SCHEMA master_catalog;

CREATE TABLE master_catalog.category (
	id bigint NOT NULL,
    name character varying NOT NULL,
	created_date timestamp with time zone DEFAULT now() NOT NULL,
    created_by character varying NOT NULL,
    modified_date timestamp with time zone,
    modified_by character varying,
    is_delete boolean NOT NULL
);

CREATE SEQUENCE master_catalog.category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE master_catalog.category_id_seq OWNED BY master_catalog.category.id;

ALTER TABLE ONLY master_catalog.category ALTER COLUMN id SET DEFAULT nextval('master_catalog.category_id_seq'::regclass);

SELECT pg_catalog.setval('master_catalog.category_id_seq', 1, false);

ALTER TABLE ONLY master_catalog.category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id);


CREATE TABLE master_catalog.product (
	id bigint NOT NULL,
    name character varying NOT NULL,
	category_id bigint NOT NULL,
	code character varying,
	description character varying,
	stock bigint,
	unit character varying,
	price_per_unit bigint NOT NULL,
	image BYTEA,
	created_date timestamp with time zone DEFAULT now() NOT NULL,
    created_by character varying NOT NULL,
    modified_date timestamp with time zone,
    modified_by character varying,
    is_delete boolean NOT NULL
);

CREATE SEQUENCE master_catalog.product_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE master_catalog.product_id_seq OWNED BY master_catalog.product.id;

ALTER TABLE ONLY master_catalog.product ALTER COLUMN id SET DEFAULT nextval('master_catalog.product_id_seq'::regclass);

SELECT pg_catalog.setval('master_catalog.product_id_seq', 1, false);

ALTER TABLE ONLY master_catalog.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (id);

ALTER TABLE ONLY master_catalog.product
    ADD CONSTRAINT category_id_fk FOREIGN KEY (category_id) REFERENCES master_catalog.category(id);


INSERT INTO master_catalog.category(name, created_date, created_by, is_delete)
VALUES
('Others', NOW(), 'tester',  false);

