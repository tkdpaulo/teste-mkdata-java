
CREATE TABLE IF NOT EXISTS public.cliente
(
    ativo boolean NOT NULL,
    data_cadastro date,
    id bigint NOT NULL DEFAULT nextval('cliente_id_seq'::regclass),
    cpf_cnpj character varying(255) COLLATE pg_catalog."default",
    nome character varying(255) COLLATE pg_catalog."default",
    rg_ie character varying(255) COLLATE pg_catalog."default",
    tipo character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT cliente_pkey PRIMARY KEY (id),
    CONSTRAINT cliente_cpf_cnpj_key UNIQUE (cpf_cnpj)
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.cliente
    OWNER to postgres;


CREATE TABLE IF NOT EXISTS public.telefone
(
    cliente_id bigint,
    id bigint NOT NULL DEFAULT nextval('telefone_id_seq'::regclass),
    ddd character varying(255) COLLATE pg_catalog."default",
    numero character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT telefone_pkey PRIMARY KEY (id),
    CONSTRAINT fk8aafha0njkoyoe3kvrwsy3g8u FOREIGN KEY (cliente_id)
    REFERENCES public.cliente (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.telefone
    OWNER to postgres;