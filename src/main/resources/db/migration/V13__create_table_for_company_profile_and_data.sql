CREATE TABLE company_profile (
    id bigint NOT NULL,
    name character varying NOT NULL,
    label character varying NOT NULL,
    sort_number int NOT NULL,
    content character varying NOT NULL,
    input_type character varying NOT NULL,
    html_preview boolean,
    created_date timestamp with time zone DEFAULT now() NOT NULL,
    created_by character varying NOT NULL,
    modified_date timestamp with time zone,
    modified_by character varying,
    is_delete boolean NOT NULL
);

CREATE SEQUENCE company_profile_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE company_profile_id_seq OWNED BY company_profile.id;

ALTER TABLE ONLY company_profile ALTER COLUMN id SET DEFAULT nextval('company_profile_id_seq'::regclass);

SELECT pg_catalog.setval('company_profile_id_seq', 1, false);

ALTER TABLE ONLY company_profile
    ADD CONSTRAINT company_profile_pkey PRIMARY KEY (id);


INSERT INTO company_profile(
	name, label, sort_number, content, input_type, html_preview, created_by, is_delete
)
VALUES
(
	'COMPANY_NAME', 'Company Name', '1', 'PT. AZNA Shoping Indonesia', 'input', false, 'SYSTEM', false
),
(
	'COMPANY_LABEL', 'Company Label', '2', 'AZNA Shop', 'input', false, 'SYSTEM', false
),
(
	'PHONE_NUMBER', 'Company Phone Number', '3', '089876543210', 'input', false, 'SYSTEM', false
),
(
	'EMAIL_SUBJECT', 'Email Content', '4', 'Your Payment Was Successful! Here Is Your Invoice!', 'input', false, 'SYSTEM', false
),
(
	'COMPANY_EMAIL_ADDRESS', 'Company Email Address', '5', 'azna@test.com', 'input', true, 'SYSTEM', false
),
(
	'COMPANY_EMAIL_SECRET', 'Company Email Secret', '6', 'qwertyuiop', 'input', true, 'SYSTEM', false
),
(
	'COMPANY_ADDRESS', 'Company Address', '7', 'Jl. Dwijaya IV RT 012 RW 01, Gandaria Utara, Kebayoran Baru', 'input', false, 'SYSTEM', false
);
