--
-- PostgreSQL database dump
--

-- Dumped from database version 13.1
-- Dumped by pg_dump version 13.1

-- Started on 2022-11-05 21:57:17

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 4 (class 2615 OID 24760)
-- Name: master_authentication; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA master_authentication;


ALTER SCHEMA master_authentication OWNER TO sdt_service;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 201 (class 1259 OID 24761)
-- Name: role; Type: TABLE; Schema: master_authentication; Owner: postgres
--

CREATE TABLE master_authentication.role (
    id bigint NOT NULL,
    role_name character varying NOT NULL,
    description character varying,
    created_by character varying NOT NULL,
    created_date timestamp with time zone NOT NULL,
    " modified_by" character varying,
    " modified_date" timestamp with time zone,
    is_delete boolean
);


ALTER TABLE master_authentication.role OWNER TO sdt_service;

--
-- TOC entry 202 (class 1259 OID 24767)
-- Name: role_id_seq; Type: SEQUENCE; Schema: master_authentication; Owner: postgres
--

CREATE SEQUENCE master_authentication.role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE master_authentication.role_id_seq OWNER TO sdt_service;

--
-- TOC entry 3026 (class 0 OID 0)
-- Dependencies: 202
-- Name: role_id_seq; Type: SEQUENCE OWNED BY; Schema: master_authentication; Owner: postgres
--

ALTER SEQUENCE master_authentication.role_id_seq OWNED BY master_authentication.role.id;


--
-- TOC entry 203 (class 1259 OID 24769)
-- Name: user; Type: TABLE; Schema: master_authentication; Owner: postgres
--

CREATE TABLE master_authentication."user" (
    id bigint NOT NULL,
    username character varying NOT NULL,
    password character varying NOT NULL,
    passwordmodified_date timestamp with time zone,
    passwordmodified_by character varying,
    login_date timestamp with time zone,
    login_ip character varying,
    last_login_date timestamp with time zone,
    last_login_ip character varying,
    lockout_date timestamp with time zone,
    created_date character varying NOT NULL,
    created_by timestamp with time zone NOT NULL,
    modified_by character varying,
    modified_date timestamp with time zone,
    is_delete boolean
);


ALTER TABLE master_authentication."user" OWNER TO sdt_service;

--
-- TOC entry 204 (class 1259 OID 24775)
-- Name: user_id_seq; Type: SEQUENCE; Schema: master_authentication; Owner: postgres
--

CREATE SEQUENCE master_authentication.user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE master_authentication.user_id_seq OWNER TO sdt_service;

--
-- TOC entry 3027 (class 0 OID 0)
-- Dependencies: 204
-- Name: user_id_seq; Type: SEQUENCE OWNED BY; Schema: master_authentication; Owner: postgres
--

ALTER SEQUENCE master_authentication.user_id_seq OWNED BY master_authentication."user".id;


--
-- TOC entry 205 (class 1259 OID 24777)
-- Name: user_role; Type: TABLE; Schema: master_authentication; Owner: postgres
--

CREATE TABLE master_authentication.user_role (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    role_id bigint NOT NULL,
    created_by character varying NOT NULL,
    created_date timestamp with time zone NOT NULL,
    modified_by character varying,
    modified_date timestamp with time zone,
    is_delete boolean
);


ALTER TABLE master_authentication.user_role OWNER TO sdt_service;

--
-- TOC entry 206 (class 1259 OID 24783)
-- Name: user_role_id_seq; Type: SEQUENCE; Schema: master_authentication; Owner: postgres
--

CREATE SEQUENCE master_authentication.user_role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE master_authentication.user_role_id_seq OWNER TO sdt_service;

--
-- TOC entry 3028 (class 0 OID 0)
-- Dependencies: 206
-- Name: user_role_id_seq; Type: SEQUENCE OWNED BY; Schema: master_authentication; Owner: postgres
--

ALTER SEQUENCE master_authentication.user_role_id_seq OWNED BY master_authentication.user_role.id;


--
-- TOC entry 207 (class 1259 OID 24785)
-- Name: user_role_role_id_seq; Type: SEQUENCE; Schema: master_authentication; Owner: postgres
--

CREATE SEQUENCE master_authentication.user_role_role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE master_authentication.user_role_role_id_seq OWNER TO sdt_service;

--
-- TOC entry 3029 (class 0 OID 0)
-- Dependencies: 207
-- Name: user_role_role_id_seq; Type: SEQUENCE OWNED BY; Schema: master_authentication; Owner: postgres
--

ALTER SEQUENCE master_authentication.user_role_role_id_seq OWNED BY master_authentication.user_role.role_id;


--
-- TOC entry 208 (class 1259 OID 24787)
-- Name: user_role_user_id_seq; Type: SEQUENCE; Schema: master_authentication; Owner: postgres
--

CREATE SEQUENCE master_authentication.user_role_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE master_authentication.user_role_user_id_seq OWNER TO sdt_service;

--
-- TOC entry 3030 (class 0 OID 0)
-- Dependencies: 208
-- Name: user_role_user_id_seq; Type: SEQUENCE OWNED BY; Schema: master_authentication; Owner: postgres
--

ALTER SEQUENCE master_authentication.user_role_user_id_seq OWNED BY master_authentication.user_role.user_id;


--
-- TOC entry 2870 (class 2604 OID 24789)
-- Name: role id; Type: DEFAULT; Schema: master_authentication; Owner: postgres
--

ALTER TABLE ONLY master_authentication.role ALTER COLUMN id SET DEFAULT nextval('master_authentication.role_id_seq'::regclass);


--
-- TOC entry 2871 (class 2604 OID 24790)
-- Name: user id; Type: DEFAULT; Schema: master_authentication; Owner: postgres
--

ALTER TABLE ONLY master_authentication."user" ALTER COLUMN id SET DEFAULT nextval('master_authentication.user_id_seq'::regclass);


--
-- TOC entry 2872 (class 2604 OID 24791)
-- Name: user_role id; Type: DEFAULT; Schema: master_authentication; Owner: postgres
--

ALTER TABLE ONLY master_authentication.user_role ALTER COLUMN id SET DEFAULT nextval('master_authentication.user_role_id_seq'::regclass);


--
-- TOC entry 2873 (class 2604 OID 24792)
-- Name: user_role user_id; Type: DEFAULT; Schema: master_authentication; Owner: postgres
--

ALTER TABLE ONLY master_authentication.user_role ALTER COLUMN user_id SET DEFAULT nextval('master_authentication.user_role_user_id_seq'::regclass);


--
-- TOC entry 2874 (class 2604 OID 24793)
-- Name: user_role role_id; Type: DEFAULT; Schema: master_authentication; Owner: postgres
--

ALTER TABLE ONLY master_authentication.user_role ALTER COLUMN role_id SET DEFAULT nextval('master_authentication.user_role_role_id_seq'::regclass);


--
-- TOC entry 3013 (class 0 OID 24761)
-- Dependencies: 201
-- Data for Name: role; Type: TABLE DATA; Schema: master_authentication; Owner: postgres
--

COPY master_authentication.role (id, role_name, description, created_by, created_date, " modified_by", " modified_date", is_delete) FROM stdin;
\.


--
-- TOC entry 3015 (class 0 OID 24769)
-- Dependencies: 203
-- Data for Name: user; Type: TABLE DATA; Schema: master_authentication; Owner: postgres
--

COPY master_authentication."user" (id, username, password, passwordmodified_date, passwordmodified_by, login_date, login_ip, last_login_date, last_login_ip, lockout_date, created_date, created_by, modified_by, modified_date, is_delete) FROM stdin;
\.


--
-- TOC entry 3017 (class 0 OID 24777)
-- Dependencies: 205
-- Data for Name: user_role; Type: TABLE DATA; Schema: master_authentication; Owner: postgres
--

COPY master_authentication.user_role (id, user_id, role_id, created_by, created_date, modified_by, modified_date, is_delete) FROM stdin;
\.


--
-- TOC entry 3031 (class 0 OID 0)
-- Dependencies: 202
-- Name: role_id_seq; Type: SEQUENCE SET; Schema: master_authentication; Owner: postgres
--

SELECT pg_catalog.setval('master_authentication.role_id_seq', 1, false);


--
-- TOC entry 3032 (class 0 OID 0)
-- Dependencies: 204
-- Name: user_id_seq; Type: SEQUENCE SET; Schema: master_authentication; Owner: postgres
--

SELECT pg_catalog.setval('master_authentication.user_id_seq', 1, false);


--
-- TOC entry 3033 (class 0 OID 0)
-- Dependencies: 206
-- Name: user_role_id_seq; Type: SEQUENCE SET; Schema: master_authentication; Owner: postgres
--

SELECT pg_catalog.setval('master_authentication.user_role_id_seq', 1, false);


--
-- TOC entry 3034 (class 0 OID 0)
-- Dependencies: 207
-- Name: user_role_role_id_seq; Type: SEQUENCE SET; Schema: master_authentication; Owner: postgres
--

SELECT pg_catalog.setval('master_authentication.user_role_role_id_seq', 1, false);


--
-- TOC entry 3035 (class 0 OID 0)
-- Dependencies: 208
-- Name: user_role_user_id_seq; Type: SEQUENCE SET; Schema: master_authentication; Owner: postgres
--

SELECT pg_catalog.setval('master_authentication.user_role_user_id_seq', 1, false);


--
-- TOC entry 2876 (class 2606 OID 24795)
-- Name: role role_pkey; Type: CONSTRAINT; Schema: master_authentication; Owner: postgres
--

ALTER TABLE ONLY master_authentication.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);


--
-- TOC entry 2878 (class 2606 OID 24797)
-- Name: user user_pkey; Type: CONSTRAINT; Schema: master_authentication; Owner: postgres
--

ALTER TABLE ONLY master_authentication."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- TOC entry 2880 (class 2606 OID 24799)
-- Name: user_role user_role_pkey; Type: CONSTRAINT; Schema: master_authentication; Owner: postgres
--

ALTER TABLE ONLY master_authentication.user_role
    ADD CONSTRAINT user_role_pkey PRIMARY KEY (id);


--
-- TOC entry 2881 (class 2606 OID 24800)
-- Name: user_role role_id_pkey; Type: FK CONSTRAINT; Schema: master_authentication; Owner: postgres
--

ALTER TABLE ONLY master_authentication.user_role
    ADD CONSTRAINT role_id_pkey FOREIGN KEY (role_id) REFERENCES master_authentication.role(id) NOT VALID;


--
-- TOC entry 2882 (class 2606 OID 24805)
-- Name: user_role user_id_pkey; Type: FK CONSTRAINT; Schema: master_authentication; Owner: postgres
--

ALTER TABLE ONLY master_authentication.user_role
    ADD CONSTRAINT user_id_pkey FOREIGN KEY (user_id) REFERENCES master_authentication."user"(id) NOT VALID;


-- Completed on 2022-11-05 21:57:18

--
-- PostgreSQL database dump complete
--

