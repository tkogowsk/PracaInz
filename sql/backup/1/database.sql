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
	id bigserial NOT NULL,
	name text NOT NULL,
	CONSTRAINT "TAB_pk" PRIMARY KEY (id)

);
-- ddl-end --
ALTER TABLE public.tab OWNER TO postgres;
-- ddl-end --

-- object: public.field | type: TABLE --
-- DROP TABLE IF EXISTS public.field CASCADE;
CREATE TABLE public.field(
	id bigserial NOT NULL,
	variant_column_id bigint NOT NULL,
	options text[],
	relation text NOT NULL,
	CONSTRAINT "FIELD_pk" PRIMARY KEY (id)

);
-- ddl-end --
COMMENT ON COLUMN public.field.relation IS 'Field relation - bigger, equals, contains etc.';
-- ddl-end --
ALTER TABLE public.field OWNER TO postgres;
-- ddl-end --

-- object: public.filter | type: TABLE --
-- DROP TABLE IF EXISTS public.filter CASCADE;
CREATE TABLE public.filter(
	id bigserial NOT NULL,
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
	id bigserial NOT NULL,
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
	user_id bigint,
	smpl_id text,
	access_type text DEFAULT 'all',
	region_id bigint,
	CONSTRAINT privilege_pk PRIMARY KEY (user_id,smpl_id)

);
-- ddl-end --
COMMENT ON COLUMN public.privilege.access_type IS 'Read/write - access indicator.';
-- ddl-end --
COMMENT ON COLUMN public.privilege.region_id IS 'Access to specific data for region.';
-- ddl-end --

-- object: public.tab_field_filter | type: TABLE --
-- DROP TABLE IF EXISTS public.tab_field_filter CASCADE;
CREATE TABLE public.tab_field_filter(
	tab_id bigint,
	field_id bigint,
	filter_id bigint,
	default_value text[],
	CONSTRAINT tab_field_filter_pk PRIMARY KEY (tab_id,field_id,filter_id)

);
-- ddl-end --
ALTER TABLE public.tab_field_filter OWNER TO postgres;
-- ddl-end --

-- object: public.variant_column | type: TABLE --
-- DROP TABLE IF EXISTS public.variant_column CASCADE;
CREATE TABLE public.variant_column(
	id bigserial NOT NULL,
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
	user_id bigint,
	variant_column_id bigint,
	column_order bigint,
	visible bool,
	CONSTRAINT user_visible_variant_column_pk PRIMARY KEY (user_id,variant_column_id)

);
-- ddl-end --
COMMENT ON COLUMN public.user_visible_variant_column.column_order IS 'Column order in application table.';
-- ddl-end --
COMMENT ON COLUMN public.user_visible_variant_column.visible IS 'Is column visible for user.';
-- ddl-end --
ALTER TABLE public.user_visible_variant_column OWNER TO postgres;
-- ddl-end --

-- object: public.user_smp_tab | type: TABLE --
-- DROP TABLE IF EXISTS public.user_smp_tab CASCADE;
CREATE TABLE public.user_smp_tab(
	smpl_id text,
	user_id bigint,
	tab_field_filter_tab_id bigint,
	tab_field_filter_field_id bigint,
	tab_field_filter_filter_id bigint,
	value text,
	CONSTRAINT user_smp_tab_pk PRIMARY KEY (user_id,tab_field_filter_tab_id,tab_field_filter_field_id,tab_field_filter_filter_id,smpl_id)

);
-- ddl-end --
ALTER TABLE public.user_smp_tab OWNER TO postgres;
-- ddl-end --

-- object: tab_fk | type: CONSTRAINT --
-- ALTER TABLE public.tab_field_filter DROP CONSTRAINT IF EXISTS tab_fk CASCADE;
ALTER TABLE public.tab_field_filter ADD CONSTRAINT tab_fk FOREIGN KEY (tab_id)
REFERENCES public.tab (id) MATCH FULL
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

-- object: user_fk | type: CONSTRAINT --
-- ALTER TABLE public.user_visible_variant_column DROP CONSTRAINT IF EXISTS user_fk CASCADE;
ALTER TABLE public.user_visible_variant_column ADD CONSTRAINT user_fk FOREIGN KEY (user_id)
REFERENCES public."user" (id) MATCH FULL
ON DELETE CASCADE ON UPDATE CASCADE;
-- ddl-end --

-- object: variant_column_fk | type: CONSTRAINT --
-- ALTER TABLE public.field DROP CONSTRAINT IF EXISTS variant_column_fk CASCADE;
ALTER TABLE public.field ADD CONSTRAINT variant_column_fk FOREIGN KEY (variant_column_id)
REFERENCES public.variant_column (id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: variant_column_fk | type: CONSTRAINT --
-- ALTER TABLE public.user_visible_variant_column DROP CONSTRAINT IF EXISTS variant_column_fk CASCADE;
ALTER TABLE public.user_visible_variant_column ADD CONSTRAINT variant_column_fk FOREIGN KEY (variant_column_id)
REFERENCES public.variant_column (id) MATCH FULL
ON DELETE CASCADE ON UPDATE CASCADE;
-- ddl-end --

-- object: user_fk | type: CONSTRAINT --
-- ALTER TABLE public.privilege DROP CONSTRAINT IF EXISTS user_fk CASCADE;
ALTER TABLE public.privilege ADD CONSTRAINT user_fk FOREIGN KEY (user_id)
REFERENCES public."user" (id) MATCH FULL
ON DELETE CASCADE ON UPDATE CASCADE;
-- ddl-end --

-- object: user_fk | type: CONSTRAINT --
-- ALTER TABLE public.user_smp_tab DROP CONSTRAINT IF EXISTS user_fk CASCADE;
ALTER TABLE public.user_smp_tab ADD CONSTRAINT user_fk FOREIGN KEY (user_id)
REFERENCES public."user" (id) MATCH FULL
ON DELETE CASCADE ON UPDATE CASCADE;
-- ddl-end --

-- object: tab_field_filter_fk | type: CONSTRAINT --
-- ALTER TABLE public.user_smp_tab DROP CONSTRAINT IF EXISTS tab_field_filter_fk CASCADE;
ALTER TABLE public.user_smp_tab ADD CONSTRAINT tab_field_filter_fk FOREIGN KEY (tab_field_filter_tab_id,tab_field_filter_field_id,tab_field_filter_filter_id)
REFERENCES public.tab_field_filter (tab_id,field_id,filter_id) MATCH FULL
ON DELETE CASCADE ON UPDATE CASCADE;
-- ddl-end --

-- object: sample_metadata_fk | type: CONSTRAINT --
-- ALTER TABLE public.privilege DROP CONSTRAINT IF EXISTS sample_metadata_fk CASCADE;
ALTER TABLE public.privilege ADD CONSTRAINT sample_metadata_fk FOREIGN KEY (smpl_id)
REFERENCES public.sample_metadata (sample_id) MATCH FULL
ON DELETE CASCADE ON UPDATE CASCADE;
-- ddl-end --

-- object: sample_metadata_fk | type: CONSTRAINT --
-- ALTER TABLE public.user_smp_tab DROP CONSTRAINT IF EXISTS sample_metadata_fk CASCADE;
ALTER TABLE public.user_smp_tab ADD CONSTRAINT sample_metadata_fk FOREIGN KEY (smpl_id)
REFERENCES public.sample_metadata (sample_id) MATCH FULL
ON DELETE CASCADE ON UPDATE CASCADE;
-- ddl-end --

-- object: public."Genotype_data_JDBC" | type: TABLE --
-- DROP TABLE IF EXISTS public."Genotype_data_JDBC" CASCADE;
CREATE TABLE public."Genotype_data_JDBC"(

);
-- ddl-end --
ALTER TABLE public."Genotype_data_JDBC" OWNER TO postgres;
-- ddl-end --


