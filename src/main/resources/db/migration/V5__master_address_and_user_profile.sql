CREATE SCHEMA master_address;

CREATE TABLE master_address.city (
    id bigint NOT NULL,
    name character varying NOT NULL,
    province_id bigint NOT NULL,
    created_date timestamp with time zone DEFAULT now() NOT NULL,
    created_by character varying NOT NULL,
    modified_date timestamp with time zone,
    modified_by character varying,
    is_delete boolean NOT NULL
);

CREATE SEQUENCE master_address.city_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE master_address.city_id_seq OWNED BY master_address.city.id;

CREATE TABLE master_address.province (
    id bigint NOT NULL,
    name character varying NOT NULL,
    region_id bigint NOT NULL,
    created_date timestamp with time zone DEFAULT now() NOT NULL,
    created_by character varying NOT NULL,
    modified_date timestamp with time zone,
    modified_by character varying,
    is_delete boolean NOT NULL
);

CREATE SEQUENCE master_address.province_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE master_address.province_id_seq OWNED BY master_address.province.id;

CREATE TABLE master_address.region (
    id bigint NOT NULL,
    name character varying NOT NULL,
    created_date timestamp with time zone DEFAULT now() NOT NULL,
    created_by character varying NOT NULL,
    modified_date timestamp with time zone,
    modified_by character varying,
    is_delete boolean NOT NULL
);

CREATE SEQUENCE master_address.region_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE master_address.region_id_seq OWNED BY master_address.region.id;

ALTER TABLE ONLY master_address.city ALTER COLUMN id SET DEFAULT nextval('master_address.city_id_seq'::regclass);

ALTER TABLE ONLY master_address.province ALTER COLUMN id SET DEFAULT nextval('master_address.province_id_seq'::regclass);

ALTER TABLE ONLY master_address.region ALTER COLUMN id SET DEFAULT nextval('master_address.region_id_seq'::regclass);


SELECT pg_catalog.setval('master_address.city_id_seq', 1, false);

SELECT pg_catalog.setval('master_address.province_id_seq', 1, false);

SELECT pg_catalog.setval('master_address.region_id_seq', 1, false);


ALTER TABLE ONLY master_address.city
    ADD CONSTRAINT city_pkey PRIMARY KEY (id);

ALTER TABLE ONLY master_address.province
    ADD CONSTRAINT province_pkey PRIMARY KEY (id);

ALTER TABLE ONLY master_address.region
    ADD CONSTRAINT region_pkey PRIMARY KEY (id);

ALTER TABLE ONLY master_address.city
    ADD CONSTRAINT province_id_pkey FOREIGN KEY (province_id) REFERENCES master_address.province(id);

ALTER TABLE ONLY master_address.province
    ADD CONSTRAINT region_id_pkey FOREIGN KEY (region_id) REFERENCES master_address.region(id);

CREATE TABLE master_authentication.user_profile (
    id bigint NOT NULL,
    name character varying NOT NULL,
    gender character varying,
    email character varying,
    is_email_verified boolean,
    mobile_phone_number character varying,
    is_mobile_phone_number_verified boolean,
    photo_url character varying,
    user_id bigint,
    address character varying,
    village character varying,
    district character varying,
    city_id bigint,
    country character varying,
    postal_code character varying,
    date_of_birth date,
    place_of_birth character varying,
    name_according_to_id_card character varying,
    id_card_number character varying,
    is_verification_user boolean,
    allergy character varying,
    job character varying,
    marital_status character varying,
    member_level character varying,
    created_date timestamp with time zone DEFAULT now(),
    created_by character varying,
    modified_date timestamp with time zone,
    modified_by character varying,
    is_delete boolean
);

CREATE SEQUENCE master_authentication.user_profile_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE master_authentication.user_profile_id_seq OWNED BY master_authentication.user_profile.id;


ALTER TABLE ONLY master_authentication.user_profile ALTER COLUMN id SET DEFAULT nextval('master_authentication.user_profile_id_seq'::regclass);


SELECT pg_catalog.setval('master_authentication.user_profile_id_seq', 1, false);


ALTER TABLE ONLY master_authentication.user_profile
    ADD CONSTRAINT user_profile_pkey PRIMARY KEY (id);

ALTER TABLE ONLY master_authentication.user_profile
    ADD CONSTRAINT city_id_fk FOREIGN KEY (city_id) REFERENCES master_address.city(id);

ALTER TABLE ONLY master_authentication.user_profile
    ADD CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES master_authentication."user"(id);

