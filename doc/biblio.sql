--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: autore; Type: TABLE; Schema: public; Owner: biblioadmin; Tablespace: 
--

CREATE TABLE autore (
    id integer NOT NULL,
    nome character varying NOT NULL,
    cognome character varying
);


ALTER TABLE autore OWNER TO biblioadmin;

--
-- Name: autore_id_seq; Type: SEQUENCE; Schema: public; Owner: biblioadmin
--

CREATE SEQUENCE autore_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE autore_id_seq OWNER TO biblioadmin;

--
-- Name: autore_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: biblioadmin
--

ALTER SEQUENCE autore_id_seq OWNED BY autore.id;


--
-- Name: commenta; Type: TABLE; Schema: public; Owner: biblioadmin; Tablespace: 
--

CREATE TABLE commenta (
    progressivo integer NOT NULL,
    utente integer,
    trascrizione integer,
    titolo character varying,
    commento character varying
);


ALTER TABLE commenta OWNER TO biblioadmin;

--
-- Name: commenta_progressivo_seq; Type: SEQUENCE; Schema: public; Owner: biblioadmin
--

CREATE SEQUENCE commenta_progressivo_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE commenta_progressivo_seq OWNER TO biblioadmin;

--
-- Name: commenta_progressivo_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: biblioadmin
--

ALTER SEQUENCE commenta_progressivo_seq OWNED BY commenta.progressivo;


--
-- Name: opera; Type: TABLE; Schema: public; Owner: biblioadmin; Tablespace: 
--

CREATE TABLE opera (
    id integer NOT NULL,
    titolo character varying NOT NULL,
    lingua character varying NOT NULL,
    anno character(4) NOT NULL,
    editore character varying,
    descrizione character varying,
    pubblicata boolean,
    num_pagine integer,
    trascrittore integer,
    acquisitore integer,
    numero_pagine integer
);


ALTER TABLE opera OWNER TO biblioadmin;

--
-- Name: opera_id_seq; Type: SEQUENCE; Schema: public; Owner: biblioadmin
--

CREATE SEQUENCE opera_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE opera_id_seq OWNER TO biblioadmin;

--
-- Name: opera_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: biblioadmin
--

ALTER SEQUENCE opera_id_seq OWNED BY opera.id;


--
-- Name: pagina; Type: TABLE; Schema: public; Owner: biblioadmin; Tablespace: 
--

CREATE TABLE pagina (
    id integer NOT NULL,
    numero character varying NOT NULL,
    path_immagine character varying,
    opera integer,
    trascrizione_validata boolean,
    upload_immagine timestamp without time zone,
    ultima_modifica_trascrizione timestamp without time zone,
    path_trascrizione character varying,
    immagine_validata boolean
);


ALTER TABLE pagina OWNER TO biblioadmin;

--
-- Name: pagina_id_seq; Type: SEQUENCE; Schema: public; Owner: biblioadmin
--

CREATE SEQUENCE pagina_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pagina_id_seq OWNER TO biblioadmin;

--
-- Name: pagina_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: biblioadmin
--

ALTER SEQUENCE pagina_id_seq OWNED BY pagina.id;


--
-- Name: privilegi; Type: TABLE; Schema: public; Owner: biblioadmin; Tablespace: 
--

CREATE TABLE privilegi (
    progressivo integer NOT NULL,
    utente integer,
    ruolo integer
);


ALTER TABLE privilegi OWNER TO biblioadmin;

--
-- Name: privilegi_progressivo_seq; Type: SEQUENCE; Schema: public; Owner: biblioadmin
--

CREATE SEQUENCE privilegi_progressivo_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE privilegi_progressivo_seq OWNER TO biblioadmin;

--
-- Name: privilegi_progressivo_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: biblioadmin
--

ALTER SEQUENCE privilegi_progressivo_seq OWNED BY privilegi.progressivo;


--
-- Name: ruolo; Type: TABLE; Schema: public; Owner: biblioadmin; Tablespace: 
--

CREATE TABLE ruolo (
    id integer NOT NULL,
    nome character varying NOT NULL,
    descrizione character varying
);


ALTER TABLE ruolo OWNER TO biblioadmin;

--
-- Name: utente; Type: TABLE; Schema: public; Owner: biblioadmin; Tablespace: 
--

CREATE TABLE utente (
    id integer NOT NULL,
    username character varying,
    password character varying NOT NULL,
    nome character varying,
    cognome character varying,
    email character varying
);


ALTER TABLE utente OWNER TO biblioadmin;

--
-- Name: utente_id_seq; Type: SEQUENCE; Schema: public; Owner: biblioadmin
--

CREATE SEQUENCE utente_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE utente_id_seq OWNER TO biblioadmin;

--
-- Name: utente_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: biblioadmin
--

ALTER SEQUENCE utente_id_seq OWNED BY utente.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: biblioadmin
--

ALTER TABLE ONLY autore ALTER COLUMN id SET DEFAULT nextval('autore_id_seq'::regclass);


--
-- Name: progressivo; Type: DEFAULT; Schema: public; Owner: biblioadmin
--

ALTER TABLE ONLY commenta ALTER COLUMN progressivo SET DEFAULT nextval('commenta_progressivo_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: biblioadmin
--

ALTER TABLE ONLY opera ALTER COLUMN id SET DEFAULT nextval('opera_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: biblioadmin
--

ALTER TABLE ONLY pagina ALTER COLUMN id SET DEFAULT nextval('pagina_id_seq'::regclass);


--
-- Name: progressivo; Type: DEFAULT; Schema: public; Owner: biblioadmin
--

ALTER TABLE ONLY privilegi ALTER COLUMN progressivo SET DEFAULT nextval('privilegi_progressivo_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: biblioadmin
--

ALTER TABLE ONLY utente ALTER COLUMN id SET DEFAULT nextval('utente_id_seq'::regclass);


--
-- Data for Name: autore; Type: TABLE DATA; Schema: public; Owner: biblioadmin
--

COPY autore (id, nome, cognome) FROM stdin;
\.


--
-- Name: autore_id_seq; Type: SEQUENCE SET; Schema: public; Owner: biblioadmin
--

SELECT pg_catalog.setval('autore_id_seq', 1, false);


--
-- Data for Name: commenta; Type: TABLE DATA; Schema: public; Owner: biblioadmin
--

COPY commenta (progressivo, utente, trascrizione, titolo, commento) FROM stdin;
\.


--
-- Name: commenta_progressivo_seq; Type: SEQUENCE SET; Schema: public; Owner: biblioadmin
--

SELECT pg_catalog.setval('commenta_progressivo_seq', 1, false);


--
-- Data for Name: opera; Type: TABLE DATA; Schema: public; Owner: biblioadmin
--

COPY opera (id, titolo, lingua, anno, editore, descrizione, pubblicata, num_pagine, trascrittore, acquisitore, numero_pagine) FROM stdin;
1	titolo opera	italiano	1900	Rizzoli Larousse	descrizione	t	5	\N	\N	10
2	titolo	mambo	2000	rizzoli	descr	f	10	\N	20	10
\.


--
-- Name: opera_id_seq; Type: SEQUENCE SET; Schema: public; Owner: biblioadmin
--

SELECT pg_catalog.setval('opera_id_seq', 2, true);


--
-- Data for Name: pagina; Type: TABLE DATA; Schema: public; Owner: biblioadmin
--

COPY pagina (id, numero, path_immagine, opera, trascrizione_validata, upload_immagine, ultima_modifica_trascrizione, path_trascrizione, immagine_validata) FROM stdin;
15	1	/home/francesco/Documents/Magistrale/OOP/PROJ/immagini-opere/il_tesoretto_1.jpg	2	f	2016-08-13 12:10:20.742	\N		f
16	2	/home/francesco/Documents/Magistrale/OOP/PROJ/immagini-opere/Aristotle_latin_manuscript.jpg	2	f	2016-08-13 12:41:53.644	\N		f
\.


--
-- Name: pagina_id_seq; Type: SEQUENCE SET; Schema: public; Owner: biblioadmin
--

SELECT pg_catalog.setval('pagina_id_seq', 16, true);


--
-- Data for Name: privilegi; Type: TABLE DATA; Schema: public; Owner: biblioadmin
--

COPY privilegi (progressivo, utente, ruolo) FROM stdin;
1	15	1
3	20	2
4	25	3
\.


--
-- Name: privilegi_progressivo_seq; Type: SEQUENCE SET; Schema: public; Owner: biblioadmin
--

SELECT pg_catalog.setval('privilegi_progressivo_seq', 4, true);


--
-- Data for Name: ruolo; Type: TABLE DATA; Schema: public; Owner: biblioadmin
--

COPY ruolo (id, nome, descrizione) FROM stdin;
1	admin	gestore generale del sistema
2	acquisitore	acquisizione-digitalizzazione delle immagini
3	trascrittore	trascrizione del testo di una pagina
4	revisore acquisizioni	revisione e verifica correttezza acquisizione
5	revisore trascrizioni	revisione e validazione della trascrizione delle pagine
\.


--
-- Data for Name: utente; Type: TABLE DATA; Schema: public; Owner: biblioadmin
--

COPY utente (id, username, password, nome, cognome, email) FROM stdin;
15	franciskittu	-9-26-111121111221650313779-91-93-102-21-105-1719-39-104	Francesco	Proietti	franciskittu@gmail.com
16	marcolino	-112-31477370-80114-96-92-102-20-37-581226874-67-70125	Marco	D\\'Ettorre	marcodettorre88@gmail.com
20	marco	1114-8352-87-2760115-103-88-125-3026-3677-112-30-34-9339	marco	marco	marcodettorre88@gmail.com
25	giutom	6239127-113-10411618-6-193-7717-66-10234-8254104-52	Giuseppe	Tomei	giutom88@gmail.com
26	franceschiello	-52-63-103-10662126-125-372171-6-671-11084-14-57-348142	Franceschiello	Secchiello	franceschiellosecchiello@univaq.it
\.


--
-- Name: utente_id_seq; Type: SEQUENCE SET; Schema: public; Owner: biblioadmin
--

SELECT pg_catalog.setval('utente_id_seq', 26, true);


--
-- Name: autore_pkey; Type: CONSTRAINT; Schema: public; Owner: biblioadmin; Tablespace: 
--

ALTER TABLE ONLY autore
    ADD CONSTRAINT autore_pkey PRIMARY KEY (id);


--
-- Name: commenta_pkey; Type: CONSTRAINT; Schema: public; Owner: biblioadmin; Tablespace: 
--

ALTER TABLE ONLY commenta
    ADD CONSTRAINT commenta_pkey PRIMARY KEY (progressivo);


--
-- Name: opera_pkey; Type: CONSTRAINT; Schema: public; Owner: biblioadmin; Tablespace: 
--

ALTER TABLE ONLY opera
    ADD CONSTRAINT opera_pkey PRIMARY KEY (id);


--
-- Name: pagina_pkey; Type: CONSTRAINT; Schema: public; Owner: biblioadmin; Tablespace: 
--

ALTER TABLE ONLY pagina
    ADD CONSTRAINT pagina_pkey PRIMARY KEY (id);


--
-- Name: privilegi_pkey; Type: CONSTRAINT; Schema: public; Owner: biblioadmin; Tablespace: 
--

ALTER TABLE ONLY privilegi
    ADD CONSTRAINT privilegi_pkey PRIMARY KEY (progressivo);


--
-- Name: ruolo_pkey; Type: CONSTRAINT; Schema: public; Owner: biblioadmin; Tablespace: 
--

ALTER TABLE ONLY ruolo
    ADD CONSTRAINT ruolo_pkey PRIMARY KEY (id);


--
-- Name: utente_pkey; Type: CONSTRAINT; Schema: public; Owner: biblioadmin; Tablespace: 
--

ALTER TABLE ONLY utente
    ADD CONSTRAINT utente_pkey PRIMARY KEY (id);


--
-- Name: utente_username_key; Type: CONSTRAINT; Schema: public; Owner: biblioadmin; Tablespace: 
--

ALTER TABLE ONLY utente
    ADD CONSTRAINT utente_username_key UNIQUE (username);


--
-- Name: commenta_utente_fkey; Type: FK CONSTRAINT; Schema: public; Owner: biblioadmin
--

ALTER TABLE ONLY commenta
    ADD CONSTRAINT commenta_utente_fkey FOREIGN KEY (utente) REFERENCES utente(id);


--
-- Name: opera_acquisitore_fkey; Type: FK CONSTRAINT; Schema: public; Owner: biblioadmin
--

ALTER TABLE ONLY opera
    ADD CONSTRAINT opera_acquisitore_fkey FOREIGN KEY (acquisitore) REFERENCES utente(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: opera_trascrittore_fkey; Type: FK CONSTRAINT; Schema: public; Owner: biblioadmin
--

ALTER TABLE ONLY opera
    ADD CONSTRAINT opera_trascrittore_fkey FOREIGN KEY (trascrittore) REFERENCES utente(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: pagina_opera_fkey; Type: FK CONSTRAINT; Schema: public; Owner: biblioadmin
--

ALTER TABLE ONLY pagina
    ADD CONSTRAINT pagina_opera_fkey FOREIGN KEY (opera) REFERENCES opera(id) ON DELETE CASCADE;


--
-- Name: privilegi_ruolo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: biblioadmin
--

ALTER TABLE ONLY privilegi
    ADD CONSTRAINT privilegi_ruolo_fkey FOREIGN KEY (ruolo) REFERENCES ruolo(id);


--
-- Name: privilegi_utente_fkey; Type: FK CONSTRAINT; Schema: public; Owner: biblioadmin
--

ALTER TABLE ONLY privilegi
    ADD CONSTRAINT privilegi_utente_fkey FOREIGN KEY (utente) REFERENCES utente(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

