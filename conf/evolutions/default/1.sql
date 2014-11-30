# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table task (
  id                        integer primary key AUTOINCREMENT,
  category_error            varchar(255),
  type_error                varchar(255),
  file_error                varchar(255),
  comment_error             varchar(255),
  label                     varchar(255),
  new_date                  timestamp,
  date_new                  varchar(255))
;




# --- !Downs

PRAGMA foreign_keys = OFF;

drop table task;

PRAGMA foreign_keys = ON;

