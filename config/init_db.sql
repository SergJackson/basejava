create table resume
(
  uuid      char(36) not null constraint resume_pkey primary key,
  full_name text
);
comment on table resume is 'BaseJava';
alter table resume owner to postgres;

create table contact
(
  id          serial   not null constraint contact_pkey primary key,
  resume_uuid char(36) not null constraint contact_resume_uuid_fk references resume on delete cascade,
  type        text     not null,
  value       text     not null
);

create unique index contact_uuid_type_index on public.contact(resume_uuid, type);

comment on table contact is 'Contacts';
alter table contact owner to postgres;


create table section
(
  id          serial   not null constraint section_pkey primary key,
  resume_uuid char(36) not null constraint section_resume_uuid_fk references resume on delete cascade,
  type        text     not null,
  content     text     not null
);

create unique index section_uuid_type_index on public.section(resume_uuid, type);

comment on table section is 'Sections';
alter table section owner to postgres;
