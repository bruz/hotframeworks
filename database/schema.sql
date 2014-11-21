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
-- Name: frameworks; Type: TABLE; Schema: public; Owner: bruz; Tablespace: 
--

CREATE TABLE frameworks (
    id integer NOT NULL,
    name character varying(100),
    language_id integer,
    description character varying(255),
    github_owner character varying(255),
    github_repo character varying(255),
    stackoverflow_tag character varying(255),
    site_url character varying(255),
    url_identifier character varying(255),
    latest_score integer,
    latest_delta integer
);


ALTER TABLE public.frameworks OWNER TO bruz;

--
-- Name: frameworks_id_seq; Type: SEQUENCE; Schema: public; Owner: bruz
--

CREATE SEQUENCE frameworks_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.frameworks_id_seq OWNER TO bruz;

--
-- Name: frameworks_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: bruz
--

ALTER SEQUENCE frameworks_id_seq OWNED BY frameworks.id;


--
-- Name: languages; Type: TABLE; Schema: public; Owner: bruz; Tablespace: 
--

CREATE TABLE languages (
    id integer NOT NULL,
    name character varying(100),
    url_identifier character varying(100),
    latest_score integer,
    latest_delta integer
);


ALTER TABLE public.languages OWNER TO bruz;

--
-- Name: languages_id_seq; Type: SEQUENCE; Schema: public; Owner: bruz
--

CREATE SEQUENCE languages_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.languages_id_seq OWNER TO bruz;

--
-- Name: languages_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: bruz
--

ALTER SEQUENCE languages_id_seq OWNED BY languages.id;


--
-- Name: lobos_migrations; Type: TABLE; Schema: public; Owner: bruz; Tablespace: 
--

CREATE TABLE lobos_migrations (
    name character varying(255)
);


ALTER TABLE public.lobos_migrations OWNER TO bruz;

--
-- Name: statistic_sets; Type: TABLE; Schema: public; Owner: bruz; Tablespace: 
--

CREATE TABLE statistic_sets (
    id integer NOT NULL,
    date date
);


ALTER TABLE public.statistic_sets OWNER TO bruz;

--
-- Name: statistic_sets_id_seq; Type: SEQUENCE; Schema: public; Owner: bruz
--

CREATE SEQUENCE statistic_sets_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.statistic_sets_id_seq OWNER TO bruz;

--
-- Name: statistic_sets_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: bruz
--

ALTER SEQUENCE statistic_sets_id_seq OWNED BY statistic_sets.id;


--
-- Name: statistics; Type: TABLE; Schema: public; Owner: bruz; Tablespace: 
--

CREATE TABLE statistics (
    id integer NOT NULL,
    statistic_set_id integer,
    framework_id integer,
    type character varying(100),
    value integer,
    score integer,
    delta integer
);


ALTER TABLE public.statistics OWNER TO bruz;

--
-- Name: statistics_id_seq; Type: SEQUENCE; Schema: public; Owner: bruz
--

CREATE SEQUENCE statistics_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.statistics_id_seq OWNER TO bruz;

--
-- Name: statistics_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: bruz
--

ALTER SEQUENCE statistics_id_seq OWNED BY statistics.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: bruz
--

ALTER TABLE ONLY frameworks ALTER COLUMN id SET DEFAULT nextval('frameworks_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: bruz
--

ALTER TABLE ONLY languages ALTER COLUMN id SET DEFAULT nextval('languages_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: bruz
--

ALTER TABLE ONLY statistic_sets ALTER COLUMN id SET DEFAULT nextval('statistic_sets_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: bruz
--

ALTER TABLE ONLY statistics ALTER COLUMN id SET DEFAULT nextval('statistics_id_seq'::regclass);


--
-- Name: frameworks_primary_key_id; Type: CONSTRAINT; Schema: public; Owner: bruz; Tablespace: 
--

ALTER TABLE ONLY frameworks
    ADD CONSTRAINT frameworks_primary_key_id PRIMARY KEY (id);


--
-- Name: frameworks_unique_name; Type: CONSTRAINT; Schema: public; Owner: bruz; Tablespace: 
--

ALTER TABLE ONLY frameworks
    ADD CONSTRAINT frameworks_unique_name UNIQUE (name);


--
-- Name: languages_primary_key_id; Type: CONSTRAINT; Schema: public; Owner: bruz; Tablespace: 
--

ALTER TABLE ONLY languages
    ADD CONSTRAINT languages_primary_key_id PRIMARY KEY (id);


--
-- Name: languages_unique_name; Type: CONSTRAINT; Schema: public; Owner: bruz; Tablespace: 
--

ALTER TABLE ONLY languages
    ADD CONSTRAINT languages_unique_name UNIQUE (name);


--
-- Name: languages_unique_url_identifier; Type: CONSTRAINT; Schema: public; Owner: bruz; Tablespace: 
--

ALTER TABLE ONLY languages
    ADD CONSTRAINT languages_unique_url_identifier UNIQUE (url_identifier);


--
-- Name: statistic_sets_primary_key_id; Type: CONSTRAINT; Schema: public; Owner: bruz; Tablespace: 
--

ALTER TABLE ONLY statistic_sets
    ADD CONSTRAINT statistic_sets_primary_key_id PRIMARY KEY (id);


--
-- Name: statistic_sets_unique_date; Type: CONSTRAINT; Schema: public; Owner: bruz; Tablespace: 
--

ALTER TABLE ONLY statistic_sets
    ADD CONSTRAINT statistic_sets_unique_date UNIQUE (date);


--
-- Name: statistics_primary_key_id; Type: CONSTRAINT; Schema: public; Owner: bruz; Tablespace: 
--

ALTER TABLE ONLY statistics
    ADD CONSTRAINT statistics_primary_key_id PRIMARY KEY (id);


--
-- Name: frameworks_language_id; Type: INDEX; Schema: public; Owner: bruz; Tablespace: 
--

CREATE INDEX frameworks_language_id ON frameworks USING btree (language_id);


--
-- Name: statistics_framework_id; Type: INDEX; Schema: public; Owner: bruz; Tablespace: 
--

CREATE INDEX statistics_framework_id ON statistics USING btree (framework_id);


--
-- Name: statistics_statistic_set_id; Type: INDEX; Schema: public; Owner: bruz; Tablespace: 
--

CREATE INDEX statistics_statistic_set_id ON statistics USING btree (statistic_set_id);


--
-- Name: statistics_type; Type: INDEX; Schema: public; Owner: bruz; Tablespace: 
--

CREATE INDEX statistics_type ON statistics USING btree (type);


--
-- PostgreSQL database dump complete
--

