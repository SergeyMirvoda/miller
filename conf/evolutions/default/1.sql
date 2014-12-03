# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table reports (
  id                        varchar(40) primary key,
  message                   varchar(255),
  url                       varchar(255),
  user                      varchar(255),
  stack                     varchar(255),
  date                      varchar(255),
  browser                   varchar(255),
  agent                     varchar(255),
  client                    varchar(255),
  version                   varchar(255),
  api_key                   varchar(255))
;




# --- !Downs

PRAGMA foreign_keys = OFF;

drop table reports;

PRAGMA foreign_keys = ON;

