-- Database generated with pgModeler (PostgreSQL Database Modeler).
-- pgModeler  version: 0.9.0-beta
-- PostgreSQL version: 9.6
-- Project Site: pgmodeler.com.br
-- Model Author: ---


-- Database creation must be done outside an multicommand file.
-- These commands were put in this file only for convenience.
-- -- object: new_database | type: DATABASE --
-- -- DROP DATABASE IF EXISTS new_database;
-- CREATE DATABASE new_database
-- ;
-- -- ddl-end --
-- 

-- object: public.tab | type: TABLE --
-- DROP TABLE IF EXISTS public.tab CASCADE;
CREATE TABLE public.tab(
	name text NOT NULL,
	id bigint NOT NULL,
	CONSTRAINT "TAB_pk" PRIMARY KEY (id)

);
-- ddl-end --
ALTER TABLE public.tab OWNER TO postgres;
-- ddl-end --

-- object: public.field | type: TABLE --
-- DROP TABLE IF EXISTS public.field CASCADE;
CREATE TABLE public.field(
	id bigint NOT NULL,
	options text[],
	id_variant_column bigint,
	CONSTRAINT "FIELD_pk" PRIMARY KEY (id)

);
-- ddl-end --
ALTER TABLE public.field OWNER TO postgres;
-- ddl-end --

-- object: public.filter | type: TABLE --
-- DROP TABLE IF EXISTS public.filter CASCADE;
CREATE TABLE public.filter(
	id bigint NOT NULL,
	name text,
	is_global bool,
	CONSTRAINT "FILTER_pk" PRIMARY KEY (id)

);
-- ddl-end --
ALTER TABLE public.filter OWNER TO postgres;
-- ddl-end --

-- object: public."user" | type: TABLE --
-- DROP TABLE IF EXISTS public."user" CASCADE;
CREATE TABLE public."user"(
	id bigint NOT NULL,
	name text NOT NULL,
	CONSTRAINT "USER_pk" PRIMARY KEY (id)

);
-- ddl-end --
ALTER TABLE public."user" OWNER TO postgres;
-- ddl-end --

-- object: public.sample_metadata | type: TABLE --
-- DROP TABLE IF EXISTS public.sample_metadata CASCADE;
CREATE TABLE public.sample_metadata(
	sample_id text NOT NULL,
	CONSTRAINT "SAMPLE_pk" PRIMARY KEY (sample_id)

);
-- ddl-end --
ALTER TABLE public.sample_metadata OWNER TO postgres;
-- ddl-end --

-- object: public.privilege | type: TABLE --
-- DROP TABLE IF EXISTS public.privilege CASCADE;
CREATE TABLE public.privilege(
	id_region bigint,
	access_type text DEFAULT all,
	id_user bigint,
	sample_id_sample_metadata text,
	CONSTRAINT privilege_pk PRIMARY KEY (id_user,sample_id_sample_metadata)

);
-- ddl-end --

-- object: user_fk | type: CONSTRAINT --
-- ALTER TABLE public.privilege DROP CONSTRAINT IF EXISTS user_fk CASCADE;
ALTER TABLE public.privilege ADD CONSTRAINT user_fk FOREIGN KEY (id_user)
REFERENCES public."user" (id) MATCH FULL
ON DELETE CASCADE ON UPDATE CASCADE;
-- ddl-end --

-- object: sample_metadata_fk | type: CONSTRAINT --
-- ALTER TABLE public.privilege DROP CONSTRAINT IF EXISTS sample_metadata_fk CASCADE;
ALTER TABLE public.privilege ADD CONSTRAINT sample_metadata_fk FOREIGN KEY (sample_id_sample_metadata)
REFERENCES public.sample_metadata (sample_id) MATCH FULL
ON DELETE CASCADE ON UPDATE CASCADE;
-- ddl-end --

-- object: public.tab_field_filter | type: TABLE --
-- DROP TABLE IF EXISTS public.tab_field_filter CASCADE;
CREATE TABLE public.tab_field_filter(
	default_value text,
	tab_id bigint,
	field_id bigint,
	filter_id bigint,
	CONSTRAINT tab_field_filter_pk PRIMARY KEY (tab_id,field_id,filter_id)

);
-- ddl-end --
ALTER TABLE public.tab_field_filter OWNER TO postgres;
-- ddl-end --

-- object: public.variant_column | type: TABLE --
-- DROP TABLE IF EXISTS public.variant_column CASCADE;
CREATE TABLE public.variant_column(
	id bigint NOT NULL,
	column_name text NOT NULL,
	fe_name text,
	type text,
	CONSTRAINT "DATA_COLUMNS_pk" PRIMARY KEY (id)

);
-- ddl-end --
ALTER TABLE public.variant_column OWNER TO postgres;
-- ddl-end --

-- object: public.user_visible_variant_column | type: TABLE --
-- DROP TABLE IF EXISTS public.user_visible_variant_column CASCADE;
CREATE TABLE public.user_visible_variant_column(
	column_order bigint,
	visible bool,
	id_user bigint,
	variant_column_id bigint,
	CONSTRAINT user_visible_variant_column_pk PRIMARY KEY (id_user,variant_column_id)

);
-- ddl-end --
ALTER TABLE public.user_visible_variant_column OWNER TO postgres;
-- ddl-end --

-- object: user_fk | type: CONSTRAINT --
-- ALTER TABLE public.user_visible_variant_column DROP CONSTRAINT IF EXISTS user_fk CASCADE;
ALTER TABLE public.user_visible_variant_column ADD CONSTRAINT user_fk FOREIGN KEY (id_user)
REFERENCES public."user" (id) MATCH FULL
ON DELETE CASCADE ON UPDATE CASCADE;
-- ddl-end --

-- object: public.user_smp_tab | type: TABLE --
-- DROP TABLE IF EXISTS public.user_smp_tab CASCADE;
CREATE TABLE public.user_smp_tab(
	value text,
	id_user bigint,
	sample_id_sample_metadata text,
	tab_id_tab_field_filter bigint,
	field_id_tab_field_filter bigint,
	filter_id_tab_field_filter bigint,
	CONSTRAINT user_smp_tab_pk PRIMARY KEY (id_user,sample_id_sample_metadata,tab_id_tab_field_filter,field_id_tab_field_filter,filter_id_tab_field_filter)

);
-- ddl-end --
ALTER TABLE public.user_smp_tab OWNER TO postgres;
-- ddl-end --

-- object: user_fk | type: CONSTRAINT --
-- ALTER TABLE public.user_smp_tab DROP CONSTRAINT IF EXISTS user_fk CASCADE;
ALTER TABLE public.user_smp_tab ADD CONSTRAINT user_fk FOREIGN KEY (id_user)
REFERENCES public."user" (id) MATCH FULL
ON DELETE CASCADE ON UPDATE CASCADE;
-- ddl-end --

-- object: sample_metadata_fk | type: CONSTRAINT --
-- ALTER TABLE public.user_smp_tab DROP CONSTRAINT IF EXISTS sample_metadata_fk CASCADE;
ALTER TABLE public.user_smp_tab ADD CONSTRAINT sample_metadata_fk FOREIGN KEY (sample_id_sample_metadata)
REFERENCES public.sample_metadata (sample_id) MATCH FULL
ON DELETE CASCADE ON UPDATE CASCADE;
-- ddl-end --

-- object: variant_column_fk | type: CONSTRAINT --
-- ALTER TABLE public.field DROP CONSTRAINT IF EXISTS variant_column_fk CASCADE;
ALTER TABLE public.field ADD CONSTRAINT variant_column_fk FOREIGN KEY (id_variant_column)
REFERENCES public.variant_column (id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: tab_fk | type: CONSTRAINT --
-- ALTER TABLE public.tab_field_filter DROP CONSTRAINT IF EXISTS tab_fk CASCADE;
ALTER TABLE public.tab_field_filter ADD CONSTRAINT tab_fk FOREIGN KEY (tab_id)
REFERENCES public.tab (id) MATCH FULL
ON DELETE CASCADE ON UPDATE CASCADE;
-- ddl-end --

-- object: tab_field_filter_fk | type: CONSTRAINT --
-- ALTER TABLE public.user_smp_tab DROP CONSTRAINT IF EXISTS tab_field_filter_fk CASCADE;
ALTER TABLE public.user_smp_tab ADD CONSTRAINT tab_field_filter_fk FOREIGN KEY (tab_id_tab_field_filter,field_id_tab_field_filter,filter_id_tab_field_filter)
REFERENCES public.tab_field_filter (tab_id,field_id,filter_id) MATCH FULL
ON DELETE CASCADE ON UPDATE CASCADE;
-- ddl-end --

-- object: field_fk | type: CONSTRAINT --
-- ALTER TABLE public.tab_field_filter DROP CONSTRAINT IF EXISTS field_fk CASCADE;
ALTER TABLE public.tab_field_filter ADD CONSTRAINT field_fk FOREIGN KEY (field_id)
REFERENCES public.field (id) MATCH FULL
ON DELETE CASCADE ON UPDATE CASCADE;
-- ddl-end --

-- object: filter_fk | type: CONSTRAINT --
-- ALTER TABLE public.tab_field_filter DROP CONSTRAINT IF EXISTS filter_fk CASCADE;
ALTER TABLE public.tab_field_filter ADD CONSTRAINT filter_fk FOREIGN KEY (filter_id)
REFERENCES public.filter (id) MATCH FULL
ON DELETE CASCADE ON UPDATE CASCADE;
-- ddl-end --

-- object: variant_column_fk | type: CONSTRAINT --
-- ALTER TABLE public.user_visible_variant_column DROP CONSTRAINT IF EXISTS variant_column_fk CASCADE;
ALTER TABLE public.user_visible_variant_column ADD CONSTRAINT variant_column_fk FOREIGN KEY (variant_column_id)
REFERENCES public.variant_column (id) MATCH FULL
ON DELETE CASCADE ON UPDATE CASCADE;
-- ddl-end --


