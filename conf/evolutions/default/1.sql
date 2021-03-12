# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table leads (
  id                            bigint auto_increment not null,
  name                          varchar(150) not null,
  email                         varchar(150),
  company                       varchar(150),
  site                          varchar(150),
  notes                         varchar(1000),
  constraint uq_leads_email unique (email),
  constraint pk_leads primary key (id)
);

create table lead_phones (
  id                            bigint auto_increment not null,
  phone                         varchar(15),
  lead_id                       bigint,
  constraint pk_lead_phones primary key (id)
);

create table lead_status (
  id                            bigint auto_increment not null,
  status                        varchar(5) not null,
  finalized_at                  timestamp,
  lead_id                       bigint,
  user_id                       bigint,
  created_at                    timestamp not null,
  constraint pk_lead_status primary key (id)
);

create table users (
  id                            bigint auto_increment not null,
  name                          varchar(150) not null,
  email                         varchar(150),
  constraint uq_users_email unique (email),
  constraint pk_users primary key (id)
);

create index ix_lead_phones_lead_id on lead_phones (lead_id);
alter table lead_phones add constraint fk_lead_phones_lead_id foreign key (lead_id) references leads (id) on delete restrict on update restrict;

create index ix_lead_status_lead_id on lead_status (lead_id);
alter table lead_status add constraint fk_lead_status_lead_id foreign key (lead_id) references leads (id) on delete restrict on update restrict;

create index ix_lead_status_user_id on lead_status (user_id);
alter table lead_status add constraint fk_lead_status_user_id foreign key (user_id) references users (id) on delete restrict on update restrict;


# --- !Downs

alter table lead_phones drop constraint if exists fk_lead_phones_lead_id;
drop index if exists ix_lead_phones_lead_id;

alter table lead_status drop constraint if exists fk_lead_status_lead_id;
drop index if exists ix_lead_status_lead_id;

alter table lead_status drop constraint if exists fk_lead_status_user_id;
drop index if exists ix_lead_status_user_id;

drop table if exists leads;

drop table if exists lead_phones;

drop table if exists lead_status;

drop table if exists users;

